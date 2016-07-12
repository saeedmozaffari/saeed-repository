package mozaffari.saeed.app.internetusing.helpers;

import java.util.Calendar;
import android.util.Log;
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


    //yyyy-MM-dd HH:mm:ss ==> date insert format
    public static String getCurrentDate() {
        G.calendar = Calendar.getInstance(); // getInstance againg for update to current date time.
        int year = G.calendar.get(Calendar.YEAR);
        int month = G.calendar.get(Calendar.MONTH);
        int day = G.calendar.get(Calendar.DAY_OF_MONTH);
        int hour = G.calendar.get(Calendar.HOUR_OF_DAY);
        int minute = G.calendar.get(Calendar.MINUTE);
        int second = G.calendar.get(Calendar.SECOND);

        String date = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
        return date;
    }


    public static String getCurrentDateNumerical() {
        G.calendar = Calendar.getInstance(); // getInstance againg for update to current date time.
        int year = G.calendar.get(Calendar.YEAR);
        int month = G.calendar.get(Calendar.MONTH);
        int day = G.calendar.get(Calendar.DAY_OF_MONTH);
        int hour = G.calendar.get(Calendar.HOUR_OF_DAY);
        int minute = G.calendar.get(Calendar.MINUTE);
        int second = G.calendar.get(Calendar.SECOND);

        String date = year + timeSetZero(month) + timeSetZero(day) + timeSetZero(hour) + timeSetZero(minute) + timeSetZero(second);
        Log.i("LOG", "date : " + date);
        return date;
    }


    private static String timeSetZero(int value) {

        String finalTime;

        if (value < 10) {
            finalTime = "0" + value;
        } else {
            finalTime = "" + value;
        }

        return finalTime;
    }


    public static String formatDate(String value) {
        value = value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + value.substring(6);
        return value;
    }


    public static String formatTime(String value) {
        value = value.substring(0, 2) + "-" + value.substring(2, 4) + "-" + value.substring(4);
        return value;
    }


    public static String formatDateTime(String value) {
        value = value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + value.substring(6, 8) + "-" + value.substring(8, 10) + "-" + value.substring(10, 12) + "-" + value.substring(12);
        return value;
    }


    public static String[] spliteAndformatDateTime(String value) {

        String year, month, day, hour, minute, second;

        year = value.substring(0, 4);
        month = value.substring(4, 6);
        day = value.substring(6, 8);
        hour = value.substring(8, 10);
        minute = value.substring(10, 12);
        second = value.substring(12);

        String[] splite = { year, month, day, hour, minute, second };
        return splite;
    }


    public static String[] spliteDateTime(String dateTime) {

        String[] splitedtime = dateTime.split("\\s+");
        String date = splitedtime[0];
        String time = splitedtime[1];

        String[] spliteDate = date.split("-");

        String year = spliteDate[0];
        String month = spliteDate[1];
        String day = spliteDate[2];

        if (Integer.parseInt(month) < 10) {
            month = "0" + month;
        }
        if (Integer.parseInt(day) < 10) {
            day = "0" + day;
        }

        String[] spliteTime = time.split(":");
        String hour = spliteTime[0];
        String minute = spliteTime[1];
        String second = spliteTime[2];

        if (Integer.parseInt(hour) < 10) {
            hour = "0" + hour;
        }
        if (Integer.parseInt(minute) < 10) {
            minute = "0" + minute;
        }
        if (Integer.parseInt(second) < 10) {
            second = "0" + second;
        }

        String[] splite = { year, month, day, hour, minute, second };
        return splite;
    }


    public static String convertTimeToNumber(String dateTime) {
        String[] splitedtime = dateTime.split("\\s+");
        String date = splitedtime[0];
        String time = splitedtime[1];

        String[] spliteDate = date.split("-");
        String[] spliteTime = time.split(":");

        String year = spliteDate[0];
        String month = spliteDate[1];
        String day = spliteDate[2];

        String hour = spliteTime[0];
        String minute = spliteTime[1];
        String second = spliteTime[2];

        String number = year + month + day + hour + minute + second;

        return number;

    }
}
