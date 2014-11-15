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

        BasicClassifier basic = new BasicClassifier(50);
        List<Comment> trainData = data.getTrain();
        
        basic.train(data.getTrain());

        ClassificationExperiment zeroVsPositive = new ClassificationExperiment(basic, new ClassificationOracle(new BinaryThresholdLabeling(0)), data.getTest());
        ClassificationExperiment greaterThan5 = new ClassificationExperiment(basic, new ClassificationOracle(new BinaryThresholdLabeling(5)), data.getTest());

        ClassificationResults basicResults = zeroVsPositive.run();

        System.out.println("Running basic classifier (blindly decide based on # words in comment)");
        System.out.println("Basic classifier results: precision " + basicResults.getPrecision() + ", recall " + basicResults.getRecall());
        NaiveBayes nb = new NaiveBayes();
        nb.train(trainData);
    }
}
