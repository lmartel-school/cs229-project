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

        Labeling moreUpvotes = new BinaryThresholdLabeling(1);

        NaiveBayes nb = new NaiveBayes(moreUpvotes);
        nb.train(data.getTrain());

        ClassificationResults exp1 = (new ClassificationExperiment(nb, new ClassificationOracle(moreUpvotes), data.getTest())).run();

        System.out.println("[RESULTS] Naive bayes with classes (karma <= ) and (karma > 0)");
        exp1.printSummary();
    }
}
