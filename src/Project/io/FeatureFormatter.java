package Project.io;

import Project.features.Feature;
import Project.features.Features;
import Project.models.Labeling;
import Project.models.Comment;

import java.util.List;

public class FeatureFormatter extends LabeledFormatter {

    private final List<Feature> features;

    public FeatureFormatter(Labeling labeling, List<Feature> features, String delim) {
        super(labeling, delim);
        this.features = features;
    }

    public FeatureFormatter(List<Feature> features, String delim){
        this(null, features, delim);
    }

    @Override
    public String getFeatures(Comment comment) {
        String list = Features.map(this.features, comment).toString();
        return list.substring(1, list.length() - 1);
    }
}
