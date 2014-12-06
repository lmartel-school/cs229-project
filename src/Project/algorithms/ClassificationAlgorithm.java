package Project.algorithms;

import Project.models.CommentClass;
import Project.models.Comment;

import java.util.List;
import java.util.Map;

public interface ClassificationAlgorithm extends LearningAlgorithm {

    public CommentClass classify(Comment comment);
    public Map<Comment, CommentClass> classify(List<Comment> comments);
}
