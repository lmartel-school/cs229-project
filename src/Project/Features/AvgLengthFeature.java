package Project.Features;

import Project.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daria on 11/15/14.
 */
public class AvgLengthFeature implements Feature {
    @Override
    public double value(Comment comment) {
        String words[] = comment.split(" ");
        int numWords = words.length;
        int totalCharacters = 0;
        for(int i = 0; i < numWords; i++)
            totalCharacters += words[i].length();

        return totalCharacters/numWords;
    }
}
