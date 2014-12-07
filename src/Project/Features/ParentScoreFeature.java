package Project.features;

import Project.models.Comment;

/**
 * Created by daria on 12/6/14.
 */
public class ParentScoreFeature implements Feature {
    @Override
    public double value(Comment comment) {
        if(comment.getParent() == null) return 1;
        return (comment.getParent()).getScore();
    }
}
