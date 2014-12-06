require 'sqlite3'
require 'sequel'
require 'csv'

DB_FILE = 'scrape.db'
SUBMISSION = 'submission'
COMMENT = 'comment'
DB = Sequel.sqlite DB_FILE

DB.create_table? :reddit_items do

    Integer :id, primary_key: true
    
    String :image_id
    String :type

    String :by
    DateTime :time
    String :text, text: true
    String :parent
    Integer :score
    
    # Only for submissions
    String :subreddit
    String :title
    String :url
    
end

Item = DB[:reddit_items]

def load_submissions!
    count = 0
    prior_count = Item.where(type: SUBMISSION).count
    puts 'Working...'
    buffer = []
    CSV.foreach('redditSubmissions.csv', col_sep: ',',  quote_char: '"') do |row|
        next if (count += 1) <= prior_count + 1 # +1 to skip headers
        if count % 1000 == 0
            Item.multi_insert(buffer)
            puts "Loaded #{buffer.length} submissions for a total of #{count}."
            buffer.clear
        end

        image_id,unixtime,rawtime,title,total_votes,reddit_id,number_of_upvotes,
            subreddit, number_of_downvotes,localtime,score,number_of_comments,username = row

        buffer.push({
            image_id: image_id,
            type: SUBMISSION,
            by: username,
            time: DateTime.parse(rawtime),
            text: nil,
            parent: nil,
            score: score,
            subreddit: subreddit,
            title: title,
            url: "IMG-#{image_id}" # dataset doesn't have urls, so we substitute image ids
        })
    end
    puts "Loaded #{count} submissions."
end


def main
    case ARGV.first
    when 'submissions'
        load_submissions!
        exit 0
    when 'comments'
        exit 0
    else
        usage
        exit 1
    end
end

def usage
    puts <<-DOC
        Usage:

        ruby parse_reddit.rb submissions        | parse submission info from csv
        ruby parse_reddit.rb comments           | parse comments on submissions from html files
    DOC
end

main
