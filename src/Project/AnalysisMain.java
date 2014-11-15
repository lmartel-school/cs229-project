package Project;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class AnalysisMain {
	
	private static String dataDirectoryPrefix = "src/data/";
	private static String pythonScriptsDirectoryPrefix = "src/python/";
	
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
        
        runPythonNaiveBayes(data.getTrain());
    }
    
    public static void writeToFile(String filenameToWriteTo, List<Comment> trainData) {
    	String pathToWriteTo = dataDirectoryPrefix + filenameToWriteTo;
    	Writer writer = null;
    	try {
    	    writer = new BufferedWriter(new OutputStreamWriter(
    	          new FileOutputStream(pathToWriteTo), "utf-8"));
//    	    System.out.println("opened writer to the file: " + pathToWriteTo);
    	    for (int i = 0; i < trainData.size(); i++) {
    	    	Comment trainExample =trainData.get(i); 
    	    	writer.write("" + trainExample.getScore() + "," + trainExample.getText() + "\n");
            }
    	} catch (IOException ex) {
    	   System.out.println("Q_Q, writer failed to open!");
    	} finally {
    	   try {writer.close();} catch (Exception ex) {
    		   System.out.println("Q_Q, writer failed to close!");
    	   }
    	}
    }
    
    public static void runPythonNaiveBayes(List<Comment> trainData) {
    	// Write training data to file
        writeToFile("trainData.csv", trainData);
        
    	String pythonNaiveBayesFilename = pythonScriptsDirectoryPrefix + "naiveBayes.py";
    	try {
    	    ProcessBuilder pb = new ProcessBuilder("python", pythonNaiveBayesFilename);
    	    Process p = pb.start();
    	    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
    	    System.out.println("python output first line is : " + in.readLine());
	    } catch(Exception e) {
	    	System.out.println(e);
    	}
    }
}
