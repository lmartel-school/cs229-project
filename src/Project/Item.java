package Project;

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
    public static final String TEXT_COL = "text";
    public static final String SCORE_COL = "score";
    public static final String PARENT_COL = "parent";
    public static final String TITLE_COL = "title";
    public static final String URL_COL = "url";
    public static final String TYPE_COL = "type";

    public static final String COMMENT_TYPE = "comment";
    public static final String STORY_TYPE = "story";
    public static final String POLL_TYPE = "poll";

    protected final int id;
    protected final String by;
    protected final long time;
    protected final String text;
    protected final int score;

    protected Item(int id, String by, long time, String text, int score) {
        this.id = id;
        this.by = by;
        this.time = time;
        this.text = text;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public String getBy() {
        return by;
    }

    public long getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public int getScore() {
        return score;
    }

    public static List<Item> fromResults(ResultSet results){
        List<Item> items = new ArrayList<Item>();
        Map<Integer, Item> itemsById = new HashMap<Integer, Item>();
        try {
            while(results.next()){
                String t = results.getString(TYPE_COL);
                int id = results.getInt(ID_COL);
                Item item;
                if(t.equals(COMMENT_TYPE)){
                    item = new Comment(
                            id,
                            results.getString(BY_COL),
                            results.getLong(TIME_COL),
                            results.getString(TEXT_COL),
                            results.getInt(SCORE_COL),
                            results.getInt(PARENT_COL)
                    );

                } else if (t.equals(STORY_TYPE) || t.equals(POLL_TYPE)) {
                    item = new Submission(
                            id,
                            results.getString(BY_COL),
                            results.getLong(TIME_COL),
                            results.getString(TEXT_COL),
                            results.getInt(SCORE_COL),
                            results.getString(TITLE_COL),
                            results.getString(URL_COL)
                    );
                } else continue; // ignore jobs and poll options
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

    public static List<Comment> getComments(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        List<Item> items = Item.fromResults(statement.executeQuery("SELECT * FROM items WHERE score IS NOT NULL"));
        List<Comment> comments = new ArrayList<Comment>();
        for (Item i : items) {
            if (i instanceof Comment) comments.add((Comment) i);
        }
        return comments;
    }

}
