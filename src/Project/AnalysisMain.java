package Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

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


        NaiveBayes nb = new NaiveBayes(threshold);
        nb.train(data.getTrain());

        BasicClassifier basic = new BasicClassifier(20);
        basic.train(data.getTrain());

        ClassificationResults basicExp = (new ClassificationExperiment(basic, new ClassificationOracle(threshold), data.getTest())).run();
        ClassificationResults nbExp = (new ClassificationExperiment(nb, new ClassificationOracle(threshold), data.getTest())).run();

        System.out.println("[RESULTS] basic binary classifier:");
        basicExp.printSummary();

        System.out.println("[RESULTS] Naive bayes binary classification:");
        nbExp.printSummary();
    }
}
