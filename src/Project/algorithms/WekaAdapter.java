package Project.algorithms;

import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Created by leo on 12/6/14.
 */
public interface WekaAdapter {
    public void evaluate(Evaluation eval, Instances test) throws UnableToEvaluateException, Exception;
    public void buildClassifier(Instances train) throws Exception;
    public double[] distributionForInstance(Instance datum) throws Exception;

    String getName();
}
