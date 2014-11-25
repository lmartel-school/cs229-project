package Project;

import Project.Features.Feature;
import Project.Features.Features;

import java.util.List;

public class FeatureFormatter extends LabeledFormatter {

    private final List<Feature> features;

    public FeatureFormatter(Labeling labeling, List<Feature> features) {
        super(labeling, ",");
        this.features = features;
    }

    public FeatureFormatter(List<Feature> features){
        super(null, ",");
        this.features = features;
    }

    @Override
    public String getFeatures(Comment comment) {
        String list = Features.map(this.features, comment).toString();
        return list.substring(1, list.length() - 1);
    }
}
