package Project.io;

import Project.models.Labeling;
import Project.models.Comment;

public abstract class LabeledFormatter implements InputFileLineFormatter {

    protected final Labeling labeling;
    private final String delim;

    public LabeledFormatter(Labeling labeling, String delim){
        this.labeling = labeling;
        this.delim = delim;
    }

    public String getLabel(Comment comment) {
        if(this.labeling == null) return Integer.toString(comment.getScore());
        return Integer.toString(this.labeling.label(comment).getValue());
    }

    public abstract String getFeatures(Comment comment);

    @Override
    public String getLine(Comment comment) {
        return getLabel(comment) + delim + getFeatures(comment) + '\n';
    }
}
