package Project;

import Project.Features.Feature;
import Project.Features.Features;
import Project.Features.RegressionFeatureVector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LinearRegression implements RegressionAlgorithm {

    private final List<Feature> features;

    public LinearRegression(List<Feature> features){

        this.features = features;
    }

    @Override
    public double predict(Comment comment) {
        return 0;
    }

    @Override
    public Map<Comment, Double> predict(List<Comment> comments) {
        return null;
    }

    @Override
    public void train(List<Comment> trainingData) {
        List<RegressionFeatureVector> inputs = Features.map(this.features, trainingData);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Config.BASE_PATH + Constants.LINEAR_REGRESSION_FILENAME)));
            for(RegressionFeatureVector input : inputs){
                writer.write(input.getY() + "|" + input.getX().toString() + '\n');
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
