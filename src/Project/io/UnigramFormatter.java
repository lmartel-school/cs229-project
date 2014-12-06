package Project.io;


import Project.models.Labeling;
import Project.models.Comment;

public class UnigramFormatter extends LabeledFormatter {

    public UnigramFormatter(Labeling labeling) {
        super(labeling, ",");
    }

    public UnigramFormatter(){
        super(null, ",");
    }

    @Override
    public String getFeatures(Comment comment) {
        return comment.getText();
    }
}
