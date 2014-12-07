package Project.features;

import Project.models.Comment;

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

    public static List<Feature> complexFeatures(){
        List<Feature> features = new ArrayList<Feature>();
        features.add(new AvgLengthFeature());
        features.add(new CharLengthFeature());
        features.add(new ContainsCapitalsFeature());
        features.add(new DepthFeature());
        features.add(new LinkFeature());
        features.add(new SentenceLengthFeature());
        features.add(new SwearFeature());
        features.add(new WordLengthFeature());
        features.add(new TimeSegmentFeature(1, 0, 5, 29));      // early morning
        features.add(new TimeSegmentFeature(5, 30, 8, 29));     // morning
        features.add(new TimeSegmentFeature(8, 30, 11, 59));    // pre-lunch
        features.add(new TimeSegmentFeature(12, 0, 12, 59));    // lunch hour
        features.add(new TimeSegmentFeature(13, 0, 17, 29));    // afternoon
        features.add(new TimeSegmentFeature(17, 30, 20, 59));   // evening
        features.add(new TimeSegmentFeature(21, 0, 22, 59));    // night
        features.add(new TimeSegmentFeature(23, 0, 24, 59));    // late night
        features.add(new ParentScoreFeature());
        features.add(new TimeSinceParentFeature());
        features.add(new ArticleScoreFeature());
        features.add(new TimeSinceArticleFeature());
        return features;
    }

    public static List<Feature> trivialFeatures(){
        List<Feature> features = new ArrayList<Feature>();
        features.add(new InterceptFeature());
        return features;
    }

    public static List<Double> map(List<Feature> features, Comment comment){
        List<Double> values = new ArrayList<Double>();
        for(Feature f : features){
            values.add(f.value(comment));
        }
        return values;
    }

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
