package Project.algorithms;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Created by leo on 12/6/14.
 */
public class WekaClassifierAdapter implements WekaAdapter {

    private final String name;
    private Classifier c;

    public WekaClassifierAdapter(Class<? extends Classifier> c) throws IllegalAccessException, InstantiationException {
        this.c = c.newInstance();
        this.name = c.getName();
    }

    @Override
    public void evaluate(Evaluation eval, Instances test) throws Exception {
        eval.evaluateModel(c, test);
    }

    @Override
    public void buildClassifier(Instances train) throws Exception {
        c.buildClassifier(train);
    }

    @Override
    public double[] distributionForInstance(Instance datum) throws Exception {
        return c.distributionForInstance(datum);
    }

    @Override
    public String getName() {
        return name;
    }
}
