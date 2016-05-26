package mozaffari.saeed.app.internetusing.helpers;

import mozaffari.saeed.app.internetusing.R;
import mozaffari.saeed.app.internetusing.tools.G;


public class HelperComputeTime {

    public static String getTimeDetails(long time) {
        long milliseconds = time;
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

        String duration = "";
        String second = G.context.getString(R.string.activity_main_second);
        String minute = G.context.getString(R.string.activity_main_minute);
        String hour = G.context.getString(R.string.activity_main_hour);

        if (hours > 0) {

            if (minutes > 0) {
                if (seconds > 0) {
                    duration = hours + " " + hour + " " + minutes + " " + minute + " " + seconds + " " + second;
                } else {
                    duration = hours + " " + hour + " " + minutes + " " + minute;
                }
            } else {
                duration = hours + " " + hour + " " + seconds + " " + second;
            }

        } else if (minutes > 0) {

            if (seconds > 0) {
                duration = minutes + " " + minute + " " + seconds + " " + second;
            } else {
                duration = minutes + " " + minute;
            }

        } else {
            duration = seconds + " " + second;
        }

        return duration;
    }
}
