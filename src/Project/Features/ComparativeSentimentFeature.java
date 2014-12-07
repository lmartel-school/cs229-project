package Project.features;

import Project.models.Comment;

/**
 * Created by leo on 12/7/14.
 */
public class ComparativeSentimentFeature implements Feature {
    @Override
    public double value(Comment comment) {
        return comment.getComparativeSentiment();
    }
}
