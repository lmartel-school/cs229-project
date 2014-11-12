package Project;

import java.util.List;

public class BasicSplitter implements DataSplitter {

    private List<Comment> data;

    public BasicSplitter(List<Comment> allData) {
        this.data = allData;
    }

    @Override
    public List<Comment> getTrain() {
        return data;
    }

    @Override
    public List<Comment> getTest() {
        return data;
    }
}
