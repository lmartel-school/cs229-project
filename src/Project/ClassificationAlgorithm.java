package Project;

import java.util.List;
import java.util.Map;

public interface ClassificationAlgorithm extends LearningAlgorithm {
    public enum CommentClass {
        BAD,
        GOOD
    }

    public CommentClass classify(Comment comment);
    public Map<Comment, CommentClass> classify(List<Comment> comments);
}
