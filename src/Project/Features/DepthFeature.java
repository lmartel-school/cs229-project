package Project.features;

import Project.models.Comment;
import Project.models.Item;
import Project.models.Submission;

/**
 * Created by daria on 11/14/14.
 */
public class DepthFeature implements Feature {

    @Override
    public double value(Comment comment) {
        return trace(comment);
    }

    private int trace(Item item){
        if(item == null || item instanceof Submission) return 0;
        return 1 + trace(((Comment) item).getParent());
    }
}
