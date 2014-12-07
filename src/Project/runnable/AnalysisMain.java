package Project.runnable;

import Project.Config;
import Project.algorithms.ClassificationAlgorithm;
import Project.algorithms.ClassificationOracle;
import Project.algorithms.WekaClassifier;
import Project.features.ClassificationFeature;
import Project.features.Feature;
import Project.features.Features;
import Project.models.*;
import Project.pipeline.*;
import weka.classifiers.Classifier;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnalysisMain {

    // Flag to move our classification threshold between (0 | positive) and (0,1 | >= 2)
    public static final boolean TOXIC_COMMENTS_ONLY = false;
	
    public static void main(String argv[]) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + Config.DB_PATH);
        List<Comment> comments = Item.getComments(connection);

        Labeling threshold;
        DataSplitter data;
        if (TOXIC_COMMENTS_ONLY){
            // For threshold = 0, our data is super uneven! Use BalancedPercentageSplitter.
            System.out.println("Running classification algorithm for score > 0");
            threshold = new BinaryThresholdLabeling(0);
            data = new BalancedPercentageSplitter(comments, 0.3, threshold);
        } else {
            // For threshold = 1, use the regular PercentageSplitter.
            System.out.println("Running classification algorithm for score > 1");
            threshold = new BinaryThresholdLabeling(1);
            data = new PercentageSplitter(comments, 0.3);

        }

//        NaiveBayesClassifier nb = new NaiveBayesClassifier(threshold);
//        nb.train(data.getTrain());
//        List<Comment> nbToClassify = new ArrayList<>(data.getTest());
//        ClassificationResults nbResults = (new ClassificationExperiment(nb, new ClassificationOracle(threshold), nbToClassify)).run();
//        System.out.println("[RESULTS] Naive bayes binary classification:");
//        nbResults.printSummary();

        List<Feature> baseFeatures = Features.complexFeatures();
        List<Feature> allFeatures = new ArrayList<>(baseFeatures);

        runWekaExperiment(Logistic.class, baseFeatures, threshold, data);
        runWekaExperiment(SMO.class, baseFeatures, threshold, data);
        runWekaExperiment(J48.class, allFeatures, threshold, data);
    }

    private static void pipe(Map<Comment, CommentClass> classifications, List<Feature> features) {
        features.add(new ClassificationFeature(classifications));
    }

    private static Map<Comment, CommentClass> runWekaExperiment(Class<? extends Classifier> klass, List<Feature> features, Labeling threshold, DataSplitter data){
        ClassificationAlgorithm algo = new WekaClassifier(SMO.class, threshold, features);
        algo.train(data.getTrain());
        ClassificationResults results = (new ClassificationExperiment(algo, new ClassificationOracle(threshold), data.getTest())).run();
        System.out.println("[RESULTS] " + klass.getName() + " binary classification:");
        results.printSummary();

        List<Comment> allData = new ArrayList<>(data.getTest());
        allData.addAll(data.getTrain());

        // Run classifier again on entire dataset for use in later classifiers
        // Suppress stdout to avoid confusing extra results
        PrintStream stdout = System.out;
        try {
            System.setOut(new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    // Suppress all output
                }
            }));
            return algo.classify(allData);
        } finally {
            System.setOut(stdout);
        }
    }
}
