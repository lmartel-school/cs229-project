sentiment = require('sentiment')
sqlite3 = require('sqlite3').verbose()
db = new sqlite3.Database('scrape.db')

TABLE = 'reddit_items'

db.run "ALTER TABLE #{TABLE} ADD COLUMN sentiment INTEGER", (err, res) ->
    console.log("[Suppressed] #{err}") if err

db.run "ALTER TABLE #{TABLE} ADD COLUMN sentiment_comparative DOUBLE", (err, res) ->
    console.log("[Suppressed] #{err}") if err


db.each "SELECT id, text, stripped_text, sentiment FROM #{TABLE} WHERE type = 'comment' AND stripped_text IS NOT NULL AND sentiment IS NULL ORDER BY time DESC", (err, row) ->
    # Don't SQL inject my shit
    res = sentiment(row.stripped_text)
    [score, comparative] = [res.score, res.comparative]
    db.run "UPDATE #{TABLE} SET sentiment = #{score}, sentiment_comparative = #{comparative} WHERE id = '#{row.id}'", (e2, r2) ->
        console.log row.stripped_text
        console.log "Positive: #{res.positive}"
        console.log "Negative: #{res.negative}"
        console.log "====="
        throw e2 if e2

