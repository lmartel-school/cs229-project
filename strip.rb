require 'sqlite3'
require 'sequel'
require 'json'

DB_FILE = 'scrape.db'
DB = Sequel.sqlite DB_FILE

DB.alter_table(:reddit_items) do
    add_column :stripped_text, String, :text => true unless DB[:reddit_items].columns.include? :stripped_text
    add_column :links, String unless DB[:reddit_items].columns.include? :stripped_text
end


@stopWords = ['a','i']

def GetStopWords
    File.open("stop_words.txt").each_line do |word|
        @stopWords.push(word.chomp)
    end
end

def makeSwearWords
    list = []
    File.open("swearWords.txt").each_line do |word|
        list.push word.strip
    end
    print list
end


Item = DB[:reddit_items]

# Strips all the nastay out
def UpdateComments
    Item.where(type: 'comment', stripped_text: nil).each do |comment|
        Item.where(id: comment[:id]).update(stripped_text: StripComments(comment[:text], comment[:id]))
    end
end

def StripComments(dirtyComment, id)
    removeLinks(dirtyComment, id)
    puts id
    return unstop(depunctuate(stripHTML(dirtyComment)))
end

def removeLinks(dirtyComment, id)
    linkArray = []
    (dirtyComment.scan /<a href=".*?"/).each do |l|
        linkArray.push(l.slice 9..-2)
    end
    Item.where(id: id).update(links: linkArray.to_json)
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
    return wordArray.join ' '
end

GetStopWords()
UpdateComments()

# makeSwearWords()
