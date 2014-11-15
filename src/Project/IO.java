package Project;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IO {

	public static void writeToFile(String pathToWriteTo, List<Comment> trainData) {

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
	
	public static List<String> readProcessOutput(Process p){
		List<String> outputLines = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    while(true){
	    	String line;
			try {
				line = in.readLine();
			} catch (IOException e) {
				System.out.println("Error in IO.java::readProcessOutput. Breaking out of loop.");
				break;
			}
	    	if (line == null) {
	    		break;
	    	}
	    	outputLines.add(line);
	    }
		return outputLines;
	}
}

	