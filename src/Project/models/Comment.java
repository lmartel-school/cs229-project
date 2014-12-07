package Project.models;

import java.util.List;

public class Comment extends Item {

    private final int sentiment;
    private final double comparativeSentiment;
    protected List<String> links;
    protected Item parent;
    protected int parentId;

    public Comment(int id, String by, long time, String rawText, String text, int score, int parentId, List<String> links, int sentiment, double comparativeSentiment) {
        super(id, by, time, rawText, text, score);
        this.parentId = parentId;
        this.links = links;
        this.sentiment = sentiment;
        this.comparativeSentiment = comparativeSentiment;
    }

    public List<String> getLinks() { return links; }

    public Item getParent() {
        return parent;
    }

    public void setParent(Item parent) {
        this.parent = parent;
    }

    public int getSentiment() {
        return sentiment;
    }

    public double getComparativeSentiment() {
        return comparativeSentiment;
    }
}
