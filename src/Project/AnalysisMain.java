package Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class AnalysisMain {
	
    public static void main(String argv[]) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + Config.DB_PATH);
        List<Comment> comments = Item.getComments(connection);
        DataSplitter data = new PercentageSplitter(comments, 0.3);

        List<Comment> testData =  data.getTest();

        NaiveBayes zeroVsPositive = new NaiveBayes(new BinaryThresholdLabeling(0));
        zeroVsPositive.train(data.getTrain());

        ClassificationResults exp1 = (new ClassificationExperiment(zeroVsPositive, new ClassificationOracle(new BinaryThresholdLabeling(0)), data.getTest())).run();

        System.out.println("[RESULTS] Naive bayes with classes (karma 0) and (karma > 0)");
        exp1.printSummary();
    }
}
