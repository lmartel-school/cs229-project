package Project.algorithms;

import hr.irb.fastRandomForest.FastRandomForest;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Created by leo on 12/6/14.
 */
public class WekaFastRandomForestAdapter implements WekaAdapter {

    private final String name;
    private FastRandomForest forest;

    public WekaFastRandomForestAdapter(FastRandomForest forest) {
        this.forest = forest;
        this.name = "FastRandomForest";
    }

    @Override
    public void evaluate(Evaluation eval, Instances test) throws Exception {
        throw new UnableToEvaluateException();
    }

    @Override
    public void buildClassifier(Instances train) throws Exception {
        forest.buildClassifier(train);
    }

    @Override
    public double[] distributionForInstance(Instance datum) throws Exception {
        return forest.distributionForInstance(datum);
    }

    @Override
    public String getName() {
        return name;
    }
}
