package Project.algorithms;

import Project.models.CommentClass;
import Project.models.Labeling;
import Project.models.Comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leo on 11/8/14.
 */
public class ClassificationOracle implements Oracle, ClassificationAlgorithm {

    private Labeling labeling;

    public ClassificationOracle(Labeling labeling){
        this.labeling = labeling;
    }

    @Override
    public void train(List<Comment> trainingData) {
        // Do nothing lol
    }

    @Override
    public CommentClass classify(Comment comment) {
        return labeling.label(comment);
    }

    @Override
    public Map<Comment, CommentClass> classify(List<Comment> comments) {
        Map<Comment, CommentClass> results = new HashMap<Comment, CommentClass>();
        for(Comment c : comments) results.put(c, this.classify(c));
        return results;
    }
}
