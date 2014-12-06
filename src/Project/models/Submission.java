package Project.models;

import Project.models.Item;

public class Submission extends Item {
    protected final String title;
    protected final String url;

    public Submission(int id, String by, long time, String rawText, String text, int score, String title, String url) {
        super(id, by, time, rawText, text, score);
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
