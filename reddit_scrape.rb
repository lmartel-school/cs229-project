require 'nokogiri'
require 'open-uri'
require 'mechanize'
require 'sequel'
require 'securerandom'

DB_FILE = 'scrape.db'
SUBMISSION = 'submission'
COMMENT = 'comment'
DB = Sequel.sqlite DB_FILE

Item = DB[:reddit_items]

FILES = Dir['../project-data/redditHtmlData/*.html']

def main
    FILES.each do |path|
        id = path.sub(/.*\//, '').chomp('.html')
        f = File.open(path)
        page = Nokogiri::HTML(f)
        @items = []
        page.css('.commentarea > .sitetable > .comment').each { |parent| save_comments(id, nil, parent) }
        Item.multi_insert(@items)
        File.rename(f, path + '.processed')
        puts "Loaded #{@items.length} comments from #{id}"

        f.close
    end
end

def save_comments(image_id, parent_id, comment_block)
	entries = comment_block.css('div.entry.unvoted')
    top_level = entries.first

    return if top_level.at('p:contains("[deleted]")')

	by = comment_block.css('a.author').inner_text
#    print top_level; puts
	score = top_level.css('span.score.unvoted').first.inner_text

	time = DateTime.parse(top_level.css('time').first.attr('datetime'))
	text = top_level.search('.usertext-body > .md').inner_text
    item = {
        id: SecureRandom.hex(12),
        type: COMMENT,
        by: by,
        time: time,
        text: text,
        parent: (parent_id or image_id),
        score: score.to_i
    }
    @items.push item

    (comment_block > '.child > .sitetable > .comment').each { |new_parent| save_comments(image_id, item[:id], new_parent) }
end

main
puts @items.length
puts @items
