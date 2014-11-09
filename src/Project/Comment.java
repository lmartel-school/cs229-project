package Project;

public class Comment extends Item {

    protected Item parent;
    protected int parentId;

    public Comment(int id, String by, long time, String text, int score, int parentId) {
        super(id, by, time, text, score);
        this.parentId = parentId;
    }

    public Item getParent() {
        return parent;
    }

    public void setParent(Item parent) {
        this.parent = parent;
    }

}
