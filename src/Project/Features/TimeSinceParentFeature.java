package Project.features;

import Project.models.Comment;

/**
 * Created by daria on 12/6/14.
 */
public class TimeSinceParentFeature implements Feature {
    @Override
    public double value(Comment comment) {
        if(comment.getParent() == null) return 0;
        return (comment.getTime() - (comment.getParent()).getTime()) / 60;
    }
}
