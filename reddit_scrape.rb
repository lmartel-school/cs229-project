require 'nokogiri'
require 'open-uri'
require 'mechanize'
require 'sequel'

DB_FILE = 'scrape.db'
SUBMISSION = 'submission'
COMMENT = 'comment'
DB = Sequel.sqlite DB_FILE

Item = DB[:reddit_items]
@filename

for files do |article|
	@filename = article.filename
	page = Mechanize.new.get(@filename)

	page.search('.sitetable > .comment').each { |parent| save_comments(nil, parent) }

	end

end

def save_comments(parent_id, comment_block)
	page.divs_with(:class => 'entry unvoted').each do |entry|
		top_level = entry.first
		by = parent.links.second.inner_text
		score = top_level.spans_with(:class => 'score unvoted').first.inner_text
		time = DateTime.new(top_level.times.first.inner_text)
		text = top_level.search('.usertext-body > p').first.inner_text
		Item.save(image_id: @filename, type: 'comment', by: by, score: score, time: time, text: text)

		new_id = Item.pluck(:id).where(time: time, by: by)
		top_level.search('.child > .sitetable > .comment') { |new_parent| save_comments(new_id, new_parent) } if id
	end
end