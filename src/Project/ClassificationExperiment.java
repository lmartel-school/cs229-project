package Project;

import java.util.List;
import java.util.Map;

public class ClassificationExperiment implements Experiment {

    private ClassificationAlgorithm algorithm;
    private ClassificationOracle oracle;
    private List<Comment> testData;

    public ClassificationExperiment(ClassificationAlgorithm algorithm, ClassificationOracle oracle, List<Comment> testData){
        this.algorithm = algorithm;
        this.oracle = oracle;
        this.testData = testData;
    }

    public ClassificationResults run(){
        Map<Comment, ClassificationAlgorithm.CommentClass> predictions = algorithm.classify(testData);
        Map<Comment, ClassificationAlgorithm.CommentClass> answers = oracle.classify(testData);

        ClassificationResults results = new ClassificationResults();

        for(Comment c : testData){
            boolean positive = predictions.get(c) == ClassificationAlgorithm.CommentClass.GOOD;
            boolean truu = predictions.get(c) == answers.get(c);
            if(truu && positive) results.truePositives++;
            if(truu && !positive) results.trueNegatives++;
            if(!truu && positive) results.falsePositives++;
            if(!truu && !positive) results.falseNegatives++;
        }

        return results;
    }
}
