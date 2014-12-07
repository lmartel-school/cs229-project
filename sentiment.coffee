sentiment = require('sentiment')
sqlite3 = require('sqlite3').verbose()
db = new sqlite3.Database('scrape.db')

db.run 'ALTER TABLE items ADD COLUMN sentiment INTEGER', (err, res) ->
    console.log("[Suppressed] #{err}") if err

db.run 'ALTER TABLE items ADD COLUMN sentiment_comparative DOUBLE', (err, res) ->
    console.log("[Suppressed] #{err}") if err


db.each 'SELECT id, text, stripped_text, sentiment FROM Items WHERE type = "comment" AND sentiment IS NULL', (err, row) ->
    # Don't SQL inject my shit
    res = sentiment(row.stripped_text)
    [score, comparative] = [res.score, res.comparative]
    db.run "UPDATE Items SET sentiment = #{score}, sentiment_comparative = #{comparative} WHERE id = #{row.id}", (err, row) ->
        console.log "Positive: #{res.positive}"
        console.log "Negative: #{res.negative}"
        console.log "====="
        throw err if err

