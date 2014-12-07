package Project.io;

import Project.features.Feature;
import Project.models.Comment;
import Project.models.CommentClass;
import Project.models.Labeling;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class IO {

    public static void writeWekaFile(String filename, List<Comment> comments, List<Feature> featuresPerComment, Labeling labeling){
        // TODO http://www.cs.waikato.ac.nz/ml/weka/arff.html

        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
            writer.write("@RELATION commentScores\n");
            HashMap<String, Integer> featureNameAppendedNums = new HashMap<>();

            // Write feature names at the header
            writer.write("@ATTRIBUTE class {" + CommentClass.getValuesAsString() + "}\n");
            for (int i = 0; i < featuresPerComment.size(); i++) {
                Feature f = featuresPerComment.get(i);
                // Create the feature names with appendedNums so repeats don't have the same name
                String featureName = f.getClass().getSimpleName();
                if (!featureNameAppendedNums.containsKey(featureName)) {
                    featureNameAppendedNums.put(featureName, 1);
                }
                int appendedNum = featureNameAppendedNums.get(featureName);
                String appendedFeatureName = featureName + appendedNum;
                featureNameAppendedNums.put(featureName, appendedNum+1);

                // Write to file
                String newLine = "@ATTRIBUTE " + appendedFeatureName + " NUMERIC\n";
                writer.write(newLine);
            }

            // Write data after the header
            writer.write("@DATA\n");
            for (int i = 0; i < comments.size(); i++) {
                // Write comment class {GOOD, BAD}
                Comment c = comments.get(i);
                CommentClass label = labeling.label(c);
                String newLine = label.getName() + ",";

                // Write other features
                for (int j = 0; j < featuresPerComment.size(); j++) {
                    Feature f = featuresPerComment.get(j);
                    newLine += f.value(c);
                    if (j != featuresPerComment.size()-1) {
                        newLine += ",";
                    }
                }
                writer.write(newLine+"\n");
            }
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void writeInputFile(String filename, InputFileLineFormatter formatter, List<Comment> data) {
        writeInputFile(filename, formatter, data, false, null);
    }

	private static void writeInputFile(String filename, InputFileLineFormatter formatter, List<Comment> data, boolean headers, List<Feature> features) {
        System.out.println(data.size() + " found");

    	try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
            if(headers){
                writer.write(new UnhelpfulHeadersFormatter(features, ",", true).getLine(null));
            }
    	    for (Comment comment : data){
    	    	writer.write(formatter.getLine(comment));
            }
            writer.close();
    	} catch (IOException ex) {
    	   ex.printStackTrace();
    	}
    }

    public static void writeInputFileWithHeaders(String filename, InputFileLineFormatter featureFormatter, List<Comment> data, List<Feature> features) {
        writeInputFile(filename, featureFormatter, data, true, features);
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

	