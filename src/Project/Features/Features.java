package Project.Features;

import Project.Comment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Features {
    private Features() { /* abstract */ }

    public static void writeFeatureMatrix(String filename, List<Feature> features, List<Comment> data) throws IOException {
        System.out.println("Mapping " + features.size() + " features onto " + data.size() + " comments.");
        System.out.println("Writing linear regression input file...");
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename)));
        for(Comment c : data){
            List<Double> values = new ArrayList<Double>();
            for(Feature f : features){
                values.add(f.value(c));
            }
            String x = values.toString();
            writer.write(c.getScore() + ", " + x.substring(1, x.length() - 1) + '\n');
        }
        writer.close();
    }

    public static List<Feature> someFeatures(){
        List<Feature> features = new ArrayList<Feature>();
        features.add(new InterceptFeature());
        features.add(new DepthFeature());
        return features;
    }

    public static List<Feature> unigramFeatures(List<Comment> comments){
        Map<String, Feature> features = new HashMap<String, Feature>();
        for (Comment comment : comments){
            for (String word : comment.getText().split(" ")){
                if(!features.containsKey(word)){
                    features.put(word, new UnigramFeature(word));
                }
            }
        }
        return new ArrayList<Feature>(features.values());
    }
}
