package Project.features;

import Project.models.Comment;
import Project.models.Item;
import Project.models.Submission;

/**
 * Created by daria on 12/6/14.
 */
public class TimeSinceArticleFeature implements Feature {
    @Override
    public double value(Comment comment) { return comment.getTime() - base_time(comment); }

    private long base_time(Item item){
        if(item instanceof Submission) return item.getTime();
        return ((Comment)item).getParent() == null ? base_time(((Comment) item).getParent()) : item.getTime();
    }
}
