package Project.features;

import Project.models.Comment;
import Project.models.CommentClass;

import java.util.Map;

/**
 * Created by leo on 12/6/14.
 */
public class ClassificationFeature implements Feature {

    private final Map<Comment, CommentClass> classifications;

    public ClassificationFeature(Map<Comment, CommentClass> classifications){
        this.classifications = classifications;
    }

    @Override
    public double value(Comment comment) {
        assert(classifications.containsKey(comment));
        return classifications.get(comment).getValue();
    }
}
