require 'nokogiri'
require 'open-uri'
require 'mechanize'
require 'sequel'
require 'securerandom'
require 'sentimental'

DB_FILE = 'scrape.db'
SUBMISSION = 'submission'
COMMENT = 'comment'
DB = Sequel.sqlite DB_FILE

Item = DB[:reddit_items]

Dir['../project-data/redditHtmlData/*.processed'].each do |f| File.rename(f, f.chomp('.processed')) end if false # use to reset files

FILES = Dir['../project-data/redditHtmlData/*.html']

DB.alter_table(:reddit_items) do
    add_column :stripped_text, String, :text => true unless DB[:reddit_items].columns.include? :stripped_text
    add_column :links, String unless DB[:reddit_items].columns.include? :links
    #add_column :sentiment, Integer unless  DB[:reddit_items].columns.include? :sentiment
    #add_column :sentiment_comparative, Double unless  DB[:reddit_items].columns.include? :sentiment_comparative
end

#ANALYZER = Sentimental.new

@stopWords = ['a','i']

def GetStopWords
    File.open("stop_words.txt").each_line do |word|
        @stopWords.push(word.chomp)
    end
end


def main
    GetStopWords()
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

    stripped_tokens = StripAndTokenize(text)
    stripped_text = stripped_tokens.join ' '
#    comparative = ANALYZER.get_score stripped_text

    item = {
        id: SecureRandom.hex(12),
        type: COMMENT,
        by: by,
        time: time,
        text: text,
        stripped_text: stripped_text,
        parent: (parent_id or image_id),
        score: score.to_i
        #sentiment: comparative * stripped_tokens.length,
        #sentiment_comparative: comparative
    }
    @items.push item

    (comment_block > '.child > .sitetable > .comment').each { |new_parent| save_comments(image_id, item[:id], new_parent) }
end

def StripAndTokenize(dirtyComment)
    return unstop(depunctuate(stripHTML(dirtyComment)))
end

def depunctuate(dirtyComment)
    return dirtyComment.downcase.gsub /[^a-z\s]/i, ''
end

def stripHTML(dirtyComment)
    return dirtyComment.gsub /<.*?>/, ' '
end

def unstop(dirtyComment)
    dirtyComment = dirtyComment.gsub /\s+/, ' '

    wordArray = []
    (dirtyComment.split).each do |word|
        wordArray.push word unless @stopWords.include? word
    end
    return wordArray
end

main
puts @items.length
puts @items
