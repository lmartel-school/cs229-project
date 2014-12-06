package Project.features;

import Project.models.Comment;

import java.util.Arrays;
import java.util.List;

/**
 * Created by daria on 11/15/14.
 */
public class SwearFeature implements Feature {

    private static final String[] swearArray = {"anal", "anus", "arse", "ass", "ballsack", "balls", "bastard", "bitch", "biatch", "bloody", "blowjob", "blow job", "bollock", "bollok", "boner", "boob", "bugger", "bum", "butt", "buttplug", "clitoris", "cock", "coon", "crap", "cunt", "damn", "dick", "dildo", "dyke", "fag", "feck", "fellate", "fellatio", "felching", "fuck", "f u c k", "fudgepacker", "fudge packer", "flange", "Goddamn", "God damn", "hell", "homo", "jerk", "jizz", "knobend", "knob end", "labia", "lmao", "lmfao", "muff", "nigger", "nigga", "omg", "penis", "piss", "poop", "prick", "pube", "pussy", "queer", "scrotum", "sex", "shit", "s hit", "sh1t", "slut", "smegma", "spunk", "tit", "tosser", "turd", "twat", "vagina", "wank", "whore", "wtf"};
    private static final List<String> swearWords = Arrays.asList(swearArray);

    @Override
    public double value(Comment comment) {
        int count = 0;
        String words[] = comment.getText().split(" ");
        for (int i = 0; i < words.length; i++) {
            if(swearWords.contains(words[i]))
                count++;
        }

        return count;
    }
}
