package Project;


public class UnigramFormatter extends LabeledFormatter {

    public UnigramFormatter(Labeling labeling) {
        super(labeling, ",");
    }

    public UnigramFormatter(){
        super(null, ",");
    }

    @Override
    public String getLabel(Comment comment){
        if(this.labeling == null) return Integer.toString(comment.getScore());
        return super.getLabel(comment);
    }

    @Override
    public String getFeatures(Comment comment) {
        return comment.getText();
    }
}
