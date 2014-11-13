cs229-project
=============

## Running the scraper

Just `ruby scrape.rb` with any reasonable version of ruby. The scraper will walk backwards in time starting from the oldest item already scraped. 

After scraping comments, run `ruby scrape.rb scores` to scrape their scores. 

You probably won't need to run the scraper at all though, since `scrape.db` is already in the repo and has about 35k items in it.

## Running the java project

(Exact instructions depend on whether you use IntelliJ or Eclipse or whatever; I use IntelliJ)

1. Import the project (or create a new project and copy the files in)
2. If it doesn't compile, the project setup got messed up. Ensure `src` is designated as the project source folder
3. If you get a sqlite driver error, add `src/jar` (the folder with one jar in it) to your project classpath
4. Change the `DB_PATH` field in the `Config` class to what it should be on your computer
5. Compile and run AnalysisMain
