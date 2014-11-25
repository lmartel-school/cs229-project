package Project.Features;

import Project.Comment;

/**
 * Created by daria on 11/15/14.
 */
public class ContainsCapitalsFeature implements Feature {
    @Override
    public double value(Comment comment) {
        String text = comment.getText();
        for(int i = 0; i < text.length(); i++) {
            if(Character.isUpperCase(text.charAt(i))) {
                return 1;
            }
        }
        return 0;
    }
}
