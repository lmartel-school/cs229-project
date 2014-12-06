package Project.pipeline;

import Project.models.Comment;

import java.util.List;

public interface DataSplitter {

    public List<Comment> getTrain();
    public List<Comment> getTest();
}
