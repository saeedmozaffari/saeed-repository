package mozaffari.saeed.app.internetusing;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import mozaffari.saeed.app.internetusing.helpers.HelperComputeInternt;
import mozaffari.saeed.app.internetusing.helpers.HelperComputeTime;
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
        TextView txtAllUse = (TextView) findViewById(R.id.txtAllUse);
        TextView txtAllUseCm = (TextView) findViewById(R.id.txtAllUseCm);

        String lastDownloadUse = "";
        String lastUploadUse = "";
        String lastDate = "";
        String lastWifiName = "";
        String lastDuration = "";
        String allDuration = "";
        String mostUseInOne = "";
        int mostDownloadUseInOne = 0;
        int mostUploadUseUseInOne = 0;
        int lastDownload = 0;
        int lastUpload = 0;
        long allTime = 0;
        long lastTime = 0;

        String allDownloadS = "";
        String allUploadS = "";
        int allDownload = 0;
        int allUpload = 0;

        String mostUseWifiName = "";
        String mostUseWifiDownloadCm = "";
        String mostUseWifiUploadCm = "";
        int mostUseWifiDownload = 0;
        int mostUseWifiUpload = 0;

        //****************************** Querys ******************************

        Cursor cursor = G.cmd.selectLastUsageDetails();
        while (cursor.moveToNext()) {
            lastDownload = cursor.getInt(cursor.getColumnIndex("download"));
            lastUpload = cursor.getInt(cursor.getColumnIndex("upload"));
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
            HelperComputeTime.getTimeDetails(cursorDurations.getInt(0));
            allTime = allTime + cursorDurations.getInt(0);
        }
        cursorDurations.close();

        Cursor cursorAllUses = G.cmd.selectAllUse();
        while (cursorAllUses.moveToNext()) {
            allDownload = cursorAllUses.getInt(cursorAllUses.getColumnIndex("download"));
            allUpload = cursorAllUses.getInt(cursorAllUses.getColumnIndex("upload"));
        }
        cursorAllUses.close();

        Cursor cursorWifiDetails = G.cmd.select("*", "wifi_details");
        while (cursorWifiDetails.moveToNext()) {
            String wifiName = cursorWifiDetails.getString(cursorWifiDetails.getColumnIndex("wifi_name"));
            int download = cursorWifiDetails.getInt(cursorWifiDetails.getColumnIndex("download"));
            int upload = cursorWifiDetails.getInt(cursorWifiDetails.getColumnIndex("upload"));

            if (download > mostUseWifiDownload) {
                mostUseWifiName = wifiName;
                mostUseWifiDownload = download;
                mostUseWifiUpload = upload;
            }
        }
        cursorWifiDetails.close();

        //****************************** Preparation Values ******************************

        mostUseWifiDownloadCm = HelperComputeInternt.internetUsedCompute(mostUseWifiDownload);
        mostUseWifiUploadCm = HelperComputeInternt.internetUsedCompute(mostUseWifiUpload);
        allDownloadS = HelperComputeInternt.internetUsedCompute(allDownload);
        allUploadS = HelperComputeInternt.internetUsedCompute(allUpload);
        lastDownloadUse = HelperComputeInternt.internetUsedCompute(lastDownload);
        lastUploadUse = HelperComputeInternt.internetUsedCompute(lastUpload);
        mostUseInOne = HelperComputeInternt.internetUsedCompute(mostDownloadUseInOne) + " دانلود و " + HelperComputeInternt.internetUsedCompute(mostUploadUseUseInOne) + " آپلود";

        lastDuration = HelperComputeTime.getTimeDetails(lastTime);
        allDuration = HelperComputeTime.getTimeDetails(allTime);

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
        txtMostUseFromWifi.setText(getString(R.string.activity_main_most_use_from_wifi));
        txtMostUseFromWifiCm.setText("شما از شبکه " + mostUseWifiName + " " + mostUseWifiDownloadCm + " دانلود و " + mostUseWifiUploadCm + " آپلود داشته اید");
        txtAllUse.setText(getString(R.string.activity_main_all_uses));
        txtAllUseCm.setText(allDownloadS + " دانلود و " + allUploadS + " آپلود");

    }

}
