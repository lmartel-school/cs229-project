package Project.Features;

import Project.Comment;

import java.util.Calendar;

/**
 * Created by daria on 12/5/14.
 */
public class TimeSegmentFeature implements Feature {

    private final int s_hour;
    private final int s_min;
    private final int e_hour;
    private final int e_min;

    public TimeSegmentFeature(int s_hour, int s_min, int e_hour, int e_min) {
        this.s_hour = s_hour;
        this.s_min = s_min;
        this.e_hour = e_hour;
        this.e_min = e_min;
    }

    @Override
    public double value(Comment comment) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(comment.getTime() * 1000);
        int c_hours = calendar.get(Calendar.HOUR_OF_DAY);
        int c_mins = calendar.get(Calendar.MINUTE);

        if (c_hours >= s_hour && c_hours <= e_hour) {
            if (c_hours == s_hour && c_mins < s_min)
                return 0;
            else if (c_hours == e_hour && c_mins > e_min)
                return 0;

            return 1;
        } else return 0;
    }


}
