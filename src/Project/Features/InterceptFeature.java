package Project.Features;

import Project.Comment;

/**
 * Created by leo on 11/14/14.
 */
public class InterceptFeature implements Feature{

    @Override
    public double value(Comment comment) {
        return 1.0;
    }
}
