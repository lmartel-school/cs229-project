package Project;

import java.util.List;

public interface DataSplitter {

    public List<Comment> getTrain();
    public List<Comment> getTest();
}
