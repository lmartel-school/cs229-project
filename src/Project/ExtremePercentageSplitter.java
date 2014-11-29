package Project;

import java.util.ArrayList;
import java.util.List;

/**
 * This splitter filters out comments with exactly 1 karma--no one has voted on them, so maybe they're "noise"?
 * NOTE: this didn't seem to really help when I tried it
 */
public class ExtremePercentageSplitter extends BalancedPercentageSplitter {
    public ExtremePercentageSplitter(List<Comment> allData, double threshold, Labeling labeling) {
        super(ExtremePercentageSplitter.removeCommentsWithNoVotes(allData), threshold, labeling);
    }

    private static List<Comment> removeCommentsWithNoVotes(List<Comment> allData){
        List<Comment> votedOn = new ArrayList<Comment>();
        for(Comment c : allData){
            if(c.getScore() != 1) votedOn.add(c);
        }
        return votedOn;
    }
}
