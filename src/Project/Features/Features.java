package Project.Features;

import Project.Comment;

import java.util.ArrayList;
import java.util.List;

public class Features {
    private Features() { /* abstract */ }

    public static List<RegressionFeatureVector> map(List<Feature> features, List<Comment> data){
        List<RegressionFeatureVector> vectors = new ArrayList<RegressionFeatureVector>();
        for(Comment c : data){
            List<Double> values = new ArrayList<Double>();
            for(Feature f : features){
                values.add(f.value(c));
            }
            vectors.add(new RegressionFeatureVector(values, c.getScore()));
        }
        return vectors;
    }

    public static List<Feature> someFeatures(){
        List<Feature> features = new ArrayList<Feature>();
        features.add(new InterceptFeature());
        features.add(new DepthFeature());

        return features;
    }
}
