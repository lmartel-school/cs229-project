package Project;

//import Project.Features.Feature;
//import Project.Features.Features;

import java.io.*;
import java.util.List;
import java.util.Map;

public class NaiveBayes implements ClassificationAlgorithm {

    public NaiveBayes(){
    	// fill in with additional features if needed
    }
    
    @Override
    public void train(List<Comment> trainingData) {
        IO.writeToFile(Config.BASE_PATH + Constants.NAIVE_BAYES_TRAIN_FILENAME, trainingData);
        
        ProcessBuilder pb = new ProcessBuilder("python", Constants.NAIVE_BAYES_UNIGRAM_TRAIN);
        try {
            System.out.println("Training naive Bayes on " + trainingData.size() + " comments...");
            Process trainingProcess = pb.start();
            BufferedReader stdOut = new BufferedReader(new InputStreamReader(trainingProcess.getInputStream()));
            BufferedReader stdErr = new BufferedReader(new InputStreamReader(trainingProcess.getErrorStream()));
            String line;
            while ((line = stdErr.readLine()) != null) {
                System.err.println("ERROR: " + line);
            }
            line = null;
            while ((line = stdOut.readLine()) != null) {
                System.out.println("Training: " + line);
            }
            trainingProcess.waitFor();
            System.out.println("Training finished with code: " + trainingProcess.exitValue());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

	@Override
	public CommentClass classify(Comment comment) {
		// TODO Auto-generated method stub
		System.err.println("Ugh just classify a list of shit okay; returning good just cause");
        return CommentClass.GOOD;
	}

	@Override
	public Map<Comment, CommentClass> classify(List<Comment> comments) {
        IO.writeToFile(Config.BASE_PATH + Constants.NAIVE_BAYES_TEST_FILENAME, comments);

        ProcessBuilder pb = new ProcessBuilder("python", Constants.NAIVE_BAYES_UNIGRAM_TEST);
        try {
            System.out.println("Testing naive Bayes on " + comments.size() + " comments...");
            Process trainingProcess = pb.start();
            BufferedReader stdOut = new BufferedReader(new InputStreamReader(trainingProcess.getInputStream()));
            BufferedReader stdErr = new BufferedReader(new InputStreamReader(trainingProcess.getErrorStream()));
            String line;
            while ((line = stdErr.readLine()) != null) {
                System.err.println("ERROR: " + line);
            }
            line = null;
            while ((line = stdOut.readLine()) != null) {
                System.out.println("Testing: " + line);
            }
            trainingProcess.waitFor();
            System.out.println("Testing finished with code: " + trainingProcess.exitValue());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
	}
}
