package Project;

import java.util.List;
import java.util.Map;

public interface RegressionAlgorithm extends LearningAlgorithm {
    public double predict(Comment comment);
    public Map<Comment, Double> predict(List<Comment> comments);
}