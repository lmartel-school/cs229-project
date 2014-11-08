require 'firebase'
require 'sqlite3'
require 'sequel'
require 'htmlentities'
require 'httparty'
require 'json'

# Comments from the last month or so don't have scores in the unofficial api,
# so we don't scrape them.
# (id 8400000 was around October 2, 2014)
LATEST_USABLE_COMMENT_ID = 8400000
DB_FILE = 'scrape.db'
BASE_URI = 'https://hacker-news.firebaseio.com/v0/'

DB = Sequel.sqlite DB_FILE
FB = Firebase::Client.new BASE_URI

DB.create_table? :items do

    ## Data returned from the API
    Integer :id, primary_key: true

    TrueClass :deleted
    String :type
    String :by
    DateTime :time
    String :text, text: true
    TrueClass :dead
    Integer :parent
    
    String :kids, text: true # JSON array of child ids, stored as string

    String :url
    Integer :score
    String :title
    
    String :parts, text: true # JSON array of part ids (for polls), stored as string

    ## Data for our own bookkeeping
    TrueClass :point_scrape_attempted
end

Item = DB[:items]

class Scraper
    def initialize(start_id, fb)
        @current_item_id = start_id
        @fb = fb
    end

    def get
        raise "Item ##{@current_item_id} already scraped" if Item[id: @current_item_id]
        response = @fb.get("item/#{@current_item_id}")
        raise "Error #{response.code}" unless response.success?
        fields = response.body
        fields['text'] = HTMLEntities.new.decode fields['text'] # De-crapify HTML
        fields['kids'] = fields['kids'].to_json if fields['kids']
        fields['parts'] = fields['parts'].to_json if fields['parts']
        fields
    end

    def next!
        raise 'abstract'
    end

end

class ForwardScraper < Scraper
    def next!
        @current_item_id += 1
    end
end

class BackwardScraper < Scraper
    def next!
        @current_item_id -= 1
    end
end

class CommentKarmaScraper
    def scrape_missing!
        count = 0
        Item.where(type: 'comment', score: nil, point_scrape_attempted: nil).order(Sequel.asc :id).each do |attrs|
            id = attrs[:id]
            puts "Scraping score for #{id}..." if (count += 1) % 10 == 1
            response = HTTParty.get("https://hn.algolia.com/api/v1/items/#{id}")
            if response.code == 200
                points = JSON.parse(response.body)['points']
                Item.where(id: id).update(score: points, point_scrape_attempted: true)
            end
        end
    end
end

def main
    case ARGV.first
    when 'scores'
        CommentKarmaScraper.new.scrape_missing!
        exit 0
    when 'usage'
        usage
        exit 1
    else
        oldest_we_have = Item.min(:id) || LATEST_USABLE_COMMENT_ID
        scraper = BackwardScraper.new(oldest_we_have - 1, FB)
    end

    count = 0
    begin
        loop do
            id = Item.insert(scraper.get)
            puts "Scraped to #{id}" if (count += 1) % 10 == 1
            scraper.next!
        end
    rescue SignalException
        puts 'Stopping.'
    end
end

def usage
    puts <<-DOC
        Usage:

        ruby scrape.rb          | scrape comments from HN API
        ruby scrape.rb scores   | scrape comment scores from unofficial API
    DOC
end

main
