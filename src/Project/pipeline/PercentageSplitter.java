package Project.pipeline;

import Project.models.Comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PercentageSplitter implements DataSplitter {

    private final List<Comment> train;
    private final List<Comment> test;

    public PercentageSplitter(List<Comment> allData, double threshold) {
        Collections.shuffle(allData);

        int limit = (int) (allData.size() * threshold);

        test = new ArrayList<Comment>();
        train = new ArrayList<Comment>();

        int i = 0;
        for(; i < limit; i++){ test.add(allData.get(i)); }
        for(; i < allData.size(); i++){ train.add(allData.get(i)); }
    }


    @Override
    public List<Comment> getTrain() {
        return train;
    }

    @Override
    public List<Comment> getTest() {
        return test;
    }
}
