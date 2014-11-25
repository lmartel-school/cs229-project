package Project;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IO {

	public static void writeInputFile(String filename, InputFileLineFormatter formatter, List<Comment> data) {
        System.out.println(data.size() + " found");
    	Writer writer = null;
    	try {
    	    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
    	    for (Comment comment : data){
    	    	writer.write(formatter.getLine(comment));
            }
            writer.close();
    	} catch (IOException ex) {
    	   ex.printStackTrace();
    	}
    }

    public static void runProcess(ProcessBuilder pb){
        try {
            Process trainingProcess = pb.start();
            BufferedReader stdOut = new BufferedReader(new InputStreamReader(trainingProcess.getInputStream()));
            BufferedReader stdErr = new BufferedReader(new InputStreamReader(trainingProcess.getErrorStream()));
            String line;
            while ((line = stdErr.readLine()) != null) {
                System.err.println("> ERROR: " + line);
            }
            while ((line = stdOut.readLine()) != null) {
                System.out.println("> " + line);
            }
            trainingProcess.waitFor();
            System.out.println("Process finished with code: " + trainingProcess.exitValue());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runPython(String binary){
        ProcessBuilder pb = new ProcessBuilder("python", binary);
        runProcess(pb);
    }

    public static Map<Comment, CommentClass> readClassificationOutputFile(String filename, List<Comment> comments){
        Map<Comment, CommentClass> classifications = new HashMap<Comment, CommentClass>();

        try {
            BufferedReader rd = new BufferedReader(new FileReader(new File(filename)));
            String line;
            for (int i = 0; (line = rd.readLine()) != null; i++){
                CommentClass prediction = CommentClass.toEnum(Integer.parseInt(line));
                assert(prediction != null);
                classifications.put(comments.get(i), prediction);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Predictions file not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classifications;
    }

    public static Map<Comment, Double> readRegressionOutputFile(String filename, List<Comment> comments){
        Map<Comment, Double> results = new HashMap<Comment, Double>();

        try {
            BufferedReader rd = new BufferedReader(new FileReader(new File(filename)));
            String line;
            for (int i = 0; (line = rd.readLine()) != null; i++){
                results.put(comments.get(i), Double.parseDouble(line));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Predictions file not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }
}

	