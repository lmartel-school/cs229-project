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

        BasicClassifier basic = new BasicClassifier(20);
        basic.train(data.getTrain());

        ClassificationResults basicExp = (new ClassificationExperiment(basic, new ClassificationOracle(moreUpvotes), data.getTest())).run();
        ClassificationResults nbExp = (new ClassificationExperiment(nb, new ClassificationOracle(moreUpvotes), data.getTest())).run();

        System.out.println("[RESULTS] basic binary classifier:");
        basicExp.printSummary();

        System.out.println("[RESULTS] Naive bayes binary classification:");
        nbExp.printSummary();
    }
}
