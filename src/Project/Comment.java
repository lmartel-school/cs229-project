package Project;

import java.util.List;

public class Comment extends Item {

    protected List<String> links;
    protected Item parent;
    protected int parentId;

    public Comment(int id, String by, long time, String rawText, String text, int score, int parentId, List<String> links) {
        super(id, by, time, rawText, text, score);
        this.parentId = parentId;
        this.links = links;
    }

    public List<String> getLinks() { return links; }

    public Item getParent() {
        return parent;
    }

    public void setParent(Item parent) {
        this.parent = parent;
    }
    
}
