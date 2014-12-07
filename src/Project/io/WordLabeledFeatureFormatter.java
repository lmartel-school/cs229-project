package Project.io;

import Project.features.Feature;
import Project.models.Comment;
import Project.models.Labeling;

import java.util.List;

/**
 * Created by leo on 12/6/14.
 */
public class WordLabeledFeatureFormatter extends FeatureFormatter {

    public WordLabeledFeatureFormatter(Labeling labeling, List<Feature> features, String delim) {
        super(labeling, features, delim);
    }

    public WordLabeledFeatureFormatter(List<Feature> features, String delim) {
        super(features, delim);
    }

    @Override
    public String getLabel(Comment comment) {
        assert(this.labeling != null);
        return this.labeling.label(comment).getName();
    }
}
