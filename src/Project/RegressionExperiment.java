package Project;

import com.sun.tools.javac.util.List;

import java.util.Map;

/**
 * Created by leo on 11/14/14.
 */
public class RegressionExperiment implements Experiment {

    private final RegressionAlgorithm algorithm;
    private final List<Comment> testData;

    public RegressionExperiment(RegressionAlgorithm algorithm, List<Comment> testData) {
        this.algorithm = algorithm;
        this.testData = testData;
    }


    @Override
    public Results run() {
        Map<Comment, Double> predictions = algorithm.predict(this.testData);
        System.out.println("Made " + predictions.size() + " predictions.");
        return null;
    }
}
