package Project;

import Project.Features.Feature;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class LinearRegression implements RegressionAlgorithm {

    private final List<Feature> features;

    public LinearRegression(List<Feature> features){
        this.features = features;
    }

    @Override
    public double predict(Comment comment) {
        System.err.println("Ugh just classify a list of shit okay");
        return 0;
    }

    @Override
    public Map<Comment, Double> predict(List<Comment> comments) {

        // Features.writeFeatureMatrix(Config.BASE_PATH + Constants.LINEAR_REGRESSION_TEST_FILENAME, this.features, comments);
        IO.writeToFile(Constants.LINEAR_REGRESSION_TEST_FILENAME, comments);

        ProcessBuilder pb = new ProcessBuilder("python", Constants.LINEAR_REGRESSION_TEST);
        try {
            System.out.println("Testing linear regression on " + comments.size() + " comments...");
            Process trainingProcess = pb.start();
            BufferedReader stdOut = new BufferedReader(new InputStreamReader(trainingProcess.getInputStream()));
            BufferedReader stdErr = new BufferedReader(new InputStreamReader(trainingProcess.getErrorStream()));
            String line;
            while ((line = stdErr.readLine()) != null) {
                System.err.println("ERROR: " + line);
            }
            line = null;
            while ((line = stdOut.readLine()) != null) {
                System.out.println("Testing: " + line);
            }
            trainingProcess.waitFor();
            System.out.println("Testing finished with code: " + trainingProcess.exitValue());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void train(List<Comment> trainingData) {

        // Features.writeFeatureMatrix(Config.BASE_PATH + Constants.LINEAR_REGRESSION_TRAIN_FILENAME, this.features, trainingData);
        IO.writeToFile(Constants.LINEAR_REGRESSION_TRAIN_FILENAME, trainingData);

        ProcessBuilder pb = new ProcessBuilder("python", Constants.LINEAR_REGRESSION_TRAIN);
        try {
            System.out.println("Training linear regression on " + trainingData.size() + " comments...");
            Process trainingProcess = pb.start();
            BufferedReader stdOut = new BufferedReader(new InputStreamReader(trainingProcess.getInputStream()));
            BufferedReader stdErr = new BufferedReader(new InputStreamReader(trainingProcess.getErrorStream()));
            String line;
            while ((line = stdErr.readLine()) != null) {
                System.err.println("ERROR: " + line);
            }
            line = null;
            while ((line = stdOut.readLine()) != null) {
                System.out.println("Training: " + line);
            }
            trainingProcess.waitFor();
            System.out.println("Training finished with code: " + trainingProcess.exitValue());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
