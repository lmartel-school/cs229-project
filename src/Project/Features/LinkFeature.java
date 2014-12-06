package Project.features;

import Project.models.Comment;

/**
 * Created by daria on 11/15/14.
 */
public class LinkFeature implements Feature {

    @Override
    public double value(Comment comment) {
        return comment.getLinks().size();
    }
}
