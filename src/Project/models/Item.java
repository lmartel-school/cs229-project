package Project.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item {

    // Column names
    public static final String ID_COL = "id";
    public static final String BY_COL = "by";
    public static final String TIME_COL = "time";
    public static final String TEXT_COL = "stripped_text";
    public static final String RAW_TEXT_COL = "text";
    public static final String SCORE_COL = "score";
    public static final String PARENT_COL = "parent";
    public static final String TITLE_COL = "title";
    public static final String URL_COL = "url";
    public static final String TYPE_COL = "type";
    public static final String LINKS_COL = "links";

    public static final String COMMENT_TYPE = "comment";
    public static final String STORY_TYPE = "story";
    public static final String POLL_TYPE = "poll";
    private static final String SENTIMENT_COL = "sentiment";
    private static final String SENTIMENT_COMPARATIVE_COL = "sentiment_comparative";


    protected final String id;

    protected final String by;
    protected final long time;
    protected final String rawText;
    protected final String text;
    protected final int score;

    protected Item(String id, String by, long time, String rawText, String text, int score) {
        this.id = id;
        this.by = by;
        this.time = time;
        this.rawText = rawText;
        this.text = text;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public String getBy() {
        return by;
    }

    public long getTime() {
        return time;
    }

    public String getText() {
        if(text == null) return rawText; // TODO fix this
        return text;
    }

    public String getRawText() { 
    	return rawText; 
	}

    public int getScore() {
        return score;
    }

    public static List<Item> fromResults(ResultSet results){
        List<Item> items = new ArrayList<Item>();
        Map<String, Item> itemsById = new HashMap<>();
        try {
            while(results.next()){
                String t = results.getString(TYPE_COL);
                String id = results.getString(ID_COL);
                Item item;
                switch (t) {
                    case COMMENT_TYPE:
                        List<String> links = stringToArray(results.getString(LINKS_COL));
                        item = new Comment(
                                id,
                                results.getString(BY_COL),
                                results.getLong(TIME_COL),
                                results.getString(RAW_TEXT_COL),
                                results.getString(TEXT_COL),
                                results.getInt(SCORE_COL),
                                results.getString(PARENT_COL),
                                links,
                                results.getInt(SENTIMENT_COL),
                                results.getDouble(SENTIMENT_COMPARATIVE_COL)
                        );

                        break;
                    case STORY_TYPE:
                    case POLL_TYPE:
                        item = new Submission(
                                id,
                                results.getString(BY_COL),
                                results.getLong(TIME_COL),
                                results.getString(RAW_TEXT_COL),
                                results.getString(TEXT_COL),
                                results.getInt(SCORE_COL),
                                results.getString(TITLE_COL),
                                results.getString(URL_COL)
                        );
                        break;
                    default:
                        continue; // ignore jobs and poll options
                }
                items.add(item);
                itemsById.put(id, item);
            }
        } catch (SQLException e) {
            System.err.println("Error while parsing SQL results into Items");
            e.printStackTrace();
        }

        for(Item item : items){
            Comment comment = item instanceof Comment ? ((Comment) item) : null;
            if(comment == null) continue;
            comment.setParent(itemsById.get(comment.parentId));
        }
        return items;
    }

    public static List<Comment> getComments(Connection conn, String query) throws SQLException {
        Statement statement = conn.createStatement();
        List<Item> items = Item.fromResults(statement.executeQuery(query));
        List<Comment> comments = new ArrayList<Comment>();
        for (Item i : items) {
            if (i instanceof Comment) comments.add((Comment) i);
        }
        return comments;
    }

    public static List<Comment> getComments(Connection conn) throws SQLException {
        return getComments(conn, "SELECT * FROM items WHERE score IS NOT NULL");
    }

    public static List<Comment> getRedditComments(Connection conn) throws SQLException {
        return getComments(conn, "SELECT * FROM reddit_items ORDER BY time DESC LIMIT 100000");
    }

    private static List<String> stringToArray(String str) {
        List<String> array = new ArrayList<String>();
        if(str == null || str.equals("[]")) return array;

        str = str.substring(2, str.length() - 1);
        while(!str.isEmpty()) {
            array.add(str.substring(0, str.indexOf('"')));
            if(str.length() - 1 > str.indexOf('"') + 3)
                str = str.substring(str.indexOf('"') + 3, str.length());
            else break;
        }
        
        return array;
    }

}
