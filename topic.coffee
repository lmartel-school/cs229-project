lda = require('lda')
sqlite3 = require('sqlite3').verbose()
db = new sqlite3.Database('scrape.db')

db.run 'ALTER TABLE items ADD COLUMN topic VARCHAR(255)', (err, res) ->
    console.log("[Suppressed] #{err}") if err

db.all 'SELECT id, text, stripped_text, topic FROM Items WHERE type = "comment" AND topic IS NULL LIMIT 100', (err, rows) ->
    # Don't SQL inject my shit
    
    console.log lda(rows.map((row)-> row.stripped_text), 1, 5)
    console.log "====="
   # db.run "UPDATE Items SET sentiment = #{score}, sentiment_comparative = #{comparative} WHERE id = #{row.id}", (err, row) ->
        #console.log "Positive: #{res.positive}"
        #console.log "Negative: #{res.negative}"
        #console.log "====="
        #throw err if err

