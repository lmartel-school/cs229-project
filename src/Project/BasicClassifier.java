package Project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leo on 11/8/14.
 */
public class BasicClassifier implements ClassificationAlgorithm {

    private int threshold;

    public BasicClassifier(int threshold){
        this.threshold = threshold;
    }

    @Override
    public CommentClass classify(Comment comment) {
        if(comment.getText().split(" ").length > threshold) return CommentClass.GOOD;
        return CommentClass.BAD;
    }

    @Override
    public Map<Comment, CommentClass> classify(List<Comment> comments) {
        Map<Comment, CommentClass> results = new HashMap<Comment, CommentClass>();
        for(Comment c : comments) results.put(c, this.classify(c));
        return results;
    }

    @Override
    public void train(List<Comment> trainingData) {
        // Noooop
    }
}
