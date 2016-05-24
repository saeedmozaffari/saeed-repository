package mozaffari.saeed.app.internetusing;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;
import mozaffari.saeed.app.internetusing.tools.G;


public class ActivityMain extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtLastUse = (TextView) findViewById(R.id.txtLastUse);
        TextView txtLastUseCm = (TextView) findViewById(R.id.txtLastUseCm);
        TextView txtLastDate = (TextView) findViewById(R.id.txtLastDate);
        TextView txtLastDateCm = (TextView) findViewById(R.id.txtLastDateCm);
        TextView txtLastTime = (TextView) findViewById(R.id.txtLastTime);
        TextView txtLastTimeCm = (TextView) findViewById(R.id.txtLastTimeCm);
        TextView txtLastWifiName = (TextView) findViewById(R.id.txtLastWifiName);
        TextView txtLastWifiNameCm = (TextView) findViewById(R.id.txtLastWifiNameCm);
        TextView txtMostUseInOne = (TextView) findViewById(R.id.txtMostUseInOne);
        TextView txtMostUseInOneCm = (TextView) findViewById(R.id.txtMostUseInOneCm);
        TextView txtMostUseTime = (TextView) findViewById(R.id.txtMostUseTime);
        TextView txtMostUseTimeCm = (TextView) findViewById(R.id.txtMostUseTimeCm);
        TextView txtMostUseFromWifi = (TextView) findViewById(R.id.txtMostUseFromWifi);
        TextView txtMostUseFromWifiCm = (TextView) findViewById(R.id.txtMostUseFromWifiCm);

        String lastDownloadUse = "";
        String lastUploadUse = "";
        String lastDate = "";
        String lastWifiName = "";
        String lastDuration = "";
        String allDuration = "";
        String mostUseFromWifi = "";
        String mostUseInOne = "";
        int mostDownloadUseInOne = 0;
        int mostUploadUseUseInOne = 0;
        int lastDownload = 0;
        int lastUpload = 0;
        int mostUse = 0;
        long allTime = 0;
        long lastTime = 0;

        //****************************** Querys ******************************

        Cursor cursor = G.cmd.selectLastUsageDetails();
        while (cursor.moveToNext()) {
            lastDownload = cursor.getInt(cursor.getColumnIndex("amount_download"));
            lastUpload = cursor.getInt(cursor.getColumnIndex("amount_upload"));
            lastDate = cursor.getString(cursor.getColumnIndex("use_date"));
            lastTime = cursor.getLong(cursor.getColumnIndex("duration"));
            lastWifiName = cursor.getString(cursor.getColumnIndex("wifi_name"));
        }
        cursor.close();

        Cursor cursorMostUse = G.cmd.selectMostUseInOneConnetion();
        while (cursorMostUse.moveToNext()) {
            mostDownloadUseInOne = cursorMostUse.getInt(0);
            mostUploadUseUseInOne = cursorMostUse.getInt(1);
        }
        cursorMostUse.close();

        Cursor cursorDurations = G.cmd.selectDurations();
        while (cursorDurations.moveToNext()) {
            getTimeDetails(cursorDurations.getInt(0));
            allTime = allTime + cursorDurations.getInt(0);
        }
        cursorDurations.close();

        //****************************** Preparation Values ******************************

        lastDownloadUse = internetDownloadCompute(lastDownload);
        lastUploadUse = internetUploadCompute(lastUpload);
        mostUseInOne = internetUploadCompute(mostDownloadUseInOne) + " دانلود و " + internetUploadCompute(mostUploadUseUseInOne) + " آپلود";

        lastDuration = getTimeDetails(lastTime);
        allDuration = getTimeDetails(allTime);

        //****************************** Set Values ******************************

        txtLastUse.setText(getString(R.string.activity_main_last_use));
        txtLastUseCm.setText(lastDownloadUse + " دانلود و " + lastUploadUse + " آپلود");
        txtLastTime.setText(getString(R.string.activity_main_last_time));
        txtLastTimeCm.setText(lastDuration);
        txtLastDate.setText(getString(R.string.activity_main_last_date));
        txtLastDateCm.setText(lastDate);
        txtLastWifiName.setText(getString(R.string.activity_main_last_wifi_name));
        txtLastWifiNameCm.setText(lastWifiName);
        txtMostUseInOne.setText(getString(R.string.activity_main_most_use));
        txtMostUseInOneCm.setText(mostUseInOne);
        txtMostUseTime.setText(getString(R.string.activity_main_all_time));
        txtMostUseTimeCm.setText(allDuration);
        //txtMostUseFromWifi.setText(getString(R.string.activity_main_most_use_from_wifi));
        txtMostUseFromWifi.setText("");
        txtMostUseFromWifiCm.setText("");

    }


    private String getTimeDetails(long time) {
        long milliseconds = time;
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

        String duration = "";
        String second = getString(R.string.activity_main_second);
        String minute = getString(R.string.activity_main_minute);
        String hour = getString(R.string.activity_main_hour);

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

        Log.i("LOG", "DURATION : " + duration);
        return duration;
    }


    private String internetDownloadCompute(int amountDownload) {
        String download = amountDownload + " کیوبایت";
        int downloadDivided;
        int dataDownloadShow;
        if (amountDownload > 1024) {
            downloadDivided = amountDownload % 1024;
            dataDownloadShow = amountDownload / 1024;
            download = dataDownloadShow + " مگابایت ," + downloadDivided + " کیوبایت";
        }
        return download;
    }


    private String internetUploadCompute(int amountUpload) {
        String upload = amountUpload + " کیوبایت";
        int uploadDivided;
        int dataUploadShow;
        if (amountUpload > 1024) {
            uploadDivided = amountUpload % 1024;
            dataUploadShow = amountUpload / 1024;
            upload = dataUploadShow + " مگابایت ," + uploadDivided + " کیوبایت";
        }
        return upload;
    }
}
