package Project.pipeline;

import Project.models.CommentClass;
import Project.models.Labeling;
import Project.models.Comment;

import java.util.*;

/**
 * Created by leo on 11/29/14.
 */
public class BalancedPercentageSplitter extends PercentageSplitter {
    public BalancedPercentageSplitter(List<Comment> allData, double threshold, Labeling labeling) {
        super(BalancedPercentageSplitter.balance(allData, labeling), threshold);
    }

    // Ex: if given 1000 class-0 and 5000 class-1, return 1000 of each
    private static List<Comment> balance(List<Comment> allData, Labeling labeling){
        Collections.shuffle(allData);

        Map<CommentClass, List<Comment>> classifications = new HashMap<CommentClass, List<Comment>>();
        for (Comment c : allData){
            CommentClass klass = labeling.label(c);
            if(!classifications.containsKey(klass)) classifications.put(klass, new ArrayList<Comment>());
            classifications.get(klass).add(c);
        }

        int minClassSize = allData.size();
        for(List<Comment> group : classifications.values()){
            minClassSize = Math.min(minClassSize, group.size());
        }

        List<Comment> balancedData = new ArrayList<Comment>();
        for(List<Comment> group : classifications.values()){
            balancedData.addAll(group.subList(0, minClassSize));
        }
        return balancedData;
    }
}
