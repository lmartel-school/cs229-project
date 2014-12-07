package Project.features;

import Project.models.Comment;

/**
 * Created by daria on 11/15/14.
 */
public class AvgWordLengthFeature implements Feature {
    @Override
    public double value(Comment comment) {
        String words[] = comment.getText().split(" ");
        int numWords = words.length;
        int totalCharacters = 0;
        for(int i = 0; i < numWords; i++)
            totalCharacters += words[i].length();

        return totalCharacters/numWords;
    }
}
