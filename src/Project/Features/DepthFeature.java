package Project.Features;

import Project.Comment;
import Project.Item;
import Project.Submission;

/**
 * Created by daria on 11/14/14.
 */
public class DepthFeature implements Feature {

    @Override
    public double value(Comment comment) {
        if((Item)comment instanceof Submission)
            return 0;
        return 1 + value((Comment)(comment.getParent()));
    }
}
