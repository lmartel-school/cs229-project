package Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class AnalysisMain {
    public static void main(String argv[]) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + Config.DB_PATH);
        List<Comment> comments = Item.getComments(connection);

        BasicClassifier basic = new BasicClassifier(50);
        basic.train(comments);

        ClassificationResults basicResults = new ClassificationExperiment(basic, new ClassificationOracle(), comments).run();

        System.out.println("Running basic classifier (blindly decide based on # words in comment)");
        System.out.println("Basic classifier results: precision " + basicResults.getPrecision() + ", recall " + basicResults.getRecall());
    }

}
