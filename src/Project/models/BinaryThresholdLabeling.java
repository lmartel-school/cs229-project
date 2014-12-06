package Project.models;

/**
 * Created by leo on 11/14/14.
 */
public class BinaryThresholdLabeling implements Labeling {

    private int threshold;

    public BinaryThresholdLabeling(int threshold){
        this.threshold = threshold;
    }

    @Override
    public CommentClass label(Comment comment) {
        if(comment.getScore() > this.threshold) return CommentClass.GOOD;
        return CommentClass.BAD;
    }
}
