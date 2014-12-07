package Project.io;

import Project.features.Feature;
import Project.models.Comment;

import java.util.List;

/**
 * Created by leo on 12/6/14.
 */
public class UnhelpfulHeadersFormatter implements InputFileLineFormatter {

    private int columns;

    public UnhelpfulHeadersFormatter(List<Feature> features, String delimiter, boolean labelsColumn){
        this.columns = features.size();
        if(labelsColumn) this.columns++;
    }

    @Override
    public String getLine(Comment comment) {
        String result = "";
        for(int i = 0; i < columns; i++){
            result += "column" + i;
            if(i < columns - 1) result += ',';
        }
        return result + '\n';
    }
}
