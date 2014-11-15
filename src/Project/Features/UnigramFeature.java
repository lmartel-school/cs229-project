package Project.Features;

import Project.Comment;

public class UnigramFeature implements Feature {

    private final String word;

    public UnigramFeature(String word){
        this.word = word;
    }

    @Override
    public double value(Comment comment) {
        if(comment.getText().contains(word)){
            return 1.0;
        } else {
            return 0.0;
        }
    }
}
