package Project;

import java.util.List;
import java.util.Map;


public class NaiveBayesAlgorithm implements ClassificationAlgorithm {
	private static String pythonDirectoryPrefix = "src/python/";
	
	public void train(List<Comment> trainingData) {
		// fill in soon!
	}
	
    public CommentClass classify(Comment comment) {
    	CommentClass cc = null;
    	// fill in soon!
    	return cc;
	}
    
    public Map<Comment, CommentClass> classify(List<Comment> comments) {
//    	Map<Comment, CommentClass> resultsMap = new Map<Comment, CommentClass>();
//    	
//    	// Write training data to file
//        IO.writeToFile("trainData.csv", trainData);
//        
//    	String pythonNaiveBayesFilename = pythonDirectoryPrefix + "naiveBayes.py";
//    	try {
//    	    ProcessBuilder pb = new ProcessBuilder("python", pythonNaiveBayesFilename);
//    	    Process p = pb.start();
//    	    List<String> processOutput = IO.readProcessOutput(p);
//	    } catch(Exception e) {
//	    	System.out.println(e);
//    	}
//    	return resultsMap;
    	return null;
	}
    
    public static void runPythonNaiveBayes(List<Comment> trainData) {
    	// Write training data to file
        IO.writeToFile("trainData.csv", trainData);
        
    	String pythonNaiveBayesFilename = pythonDirectoryPrefix + "naiveBayes.py";
    	try {
    	    ProcessBuilder pb = new ProcessBuilder("python", pythonNaiveBayesFilename);
    	    Process p = pb.start();
    	    List<String> processOutput = IO.readProcessOutput(p);
	    } catch(Exception e) {
	    	System.out.println(e);
    	}
    }
}
