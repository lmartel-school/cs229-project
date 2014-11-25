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
        Map<Comment, CommentClass> predictions = algorithm.classify(testData);
        Map<Comment, CommentClass> answers = oracle.classify(testData);

        System.out.println("Made " + predictions.size() + " predictions.");

        ClassificationResults results = new ClassificationResults();

        for(Comment c : testData){
            CommentClass prediction = predictions.get(c);
            CommentClass answer = answers.get(c);

            boolean positive = prediction.getValue() == CommentClass.GOOD.getValue();
            boolean truu = prediction.getValue() == answer.getValue();

            if(truu && positive) results.truePositives++;
            if(truu && !positive) results.trueNegatives++;
            if(!truu && positive) results.falsePositives++;
            if(!truu && !positive) results.falseNegatives++;
        }

        return results;
    }
}
