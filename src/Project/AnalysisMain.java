package Project;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class AnalysisMain {
	
	private static String dataDirectoryPrefix = "src/data/";
    public static void main(String argv[]) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + Config.DB_PATH);
        List<Comment> comments = Item.getComments(connection);
        DataSplitter data = new BasicSplitter(comments);

        BasicClassifier basic = new BasicClassifier(50);
        List<Comment> trainData = data.getTrain();
        writeToFile("trainData.csv", trainData);
        
        basic.train(data.getTrain());

        ClassificationExperiment zeroVsPositive = new ClassificationExperiment(basic, new ClassificationOracle(new BinaryThresholdLabeling(0)), data.getTest());
        ClassificationExperiment greaterThan5 = new ClassificationExperiment(basic, new ClassificationOracle(new BinaryThresholdLabeling(5)), data.getTest());

        ClassificationResults basicResults = zeroVsPositive.run();

        System.out.println("Running basic classifier (blindly decide based on # words in comment)");
        System.out.println("Basic classifier results: precision " + basicResults.getPrecision() + ", recall " + basicResults.getRecall());

        // RegressionAlgorithm linReg = new LinearRegression();
    }
    
    public static void writeToFile(String filename, List<Comment> data) {
    	Writer writer = null;
    	try {
    	    writer = new BufferedWriter(new OutputStreamWriter(
    	          new FileOutputStream(dataDirectoryPrefix + filename), "utf-8"));
    	    System.out.println("opened writer to the file: " + filename);
    	    for (int i = 0; i < data.size(); i++) {
    	    	writer.write(data.get(i).getText());
            }
    	    writer.flush();
    	} catch (IOException ex) {
    	   System.out.println("Q_Q, writer failed!");
    	} finally {
    	   try {writer.close();} catch (Exception ex) {}
    	}
    }
}
