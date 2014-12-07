package Project.features;

import Project.models.Comment;
import Project.models.Item;
import Project.models.Submission;

/**
 * Created by daria on 12/6/14.
 */
public class ArticleScoreFeature implements Feature {
    @Override
    public double value(Comment comment) { return base_score(comment); }

    private int base_score(Item item){
        if(item instanceof Submission) return item.getScore();
        return ((Comment)item).getParent() == null ? base_score(((Comment) item).getParent()) : 1;
    }
}
