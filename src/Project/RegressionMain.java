package Project;

import Project.Features.Features;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class RegressionMain {
    public static void main(String argv[]) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + Config.DB_PATH);
        List<Comment> comments = Item.getComments(connection);
        DataSplitter data = new PercentageSplitter(comments, 0.3);

        RegressionAlgorithm linReg = new LinearRegression(Features.complexFeatures());
        linReg.train(data.getTrain());

        linReg.predict(data.getTest());

    }
}
