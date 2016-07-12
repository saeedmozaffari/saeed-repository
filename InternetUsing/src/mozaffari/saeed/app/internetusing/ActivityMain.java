package mozaffari.saeed.app.internetusing;

import java.util.Calendar;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import mozaffari.saeed.app.internetusing.drawer.DrawerActivity;
import mozaffari.saeed.app.internetusing.helpers.HelperComputeInternt;
import mozaffari.saeed.app.internetusing.helpers.HelperComputeTime;
import mozaffari.saeed.app.internetusing.tools.G;


public class ActivityMain extends DrawerActivity {

    TextView txtFromDate;
    TextView txtFromClock;
    TextView txtToDate;
    TextView txtToClock;
    TextView txtUseType;

    String   fromDate;
    String   toDate;
    String   fromClock;
    String   toClock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        set();

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

        Button btnUseInternet = (Button) findViewById(R.id.btnUseInternet);

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
            lastDate = HelperComputeTime.formatDate(cursor.getString(cursor.getColumnIndex("use_date_start")));
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

        mostUseWifiDownloadCm = HelperComputeInternt.internetUsedComputeFa(mostUseWifiDownload);
        mostUseWifiUploadCm = HelperComputeInternt.internetUsedComputeFa(mostUseWifiUpload);
        allDownloadS = HelperComputeInternt.internetUsedComputeFa(allDownload);
        allUploadS = HelperComputeInternt.internetUsedComputeFa(allUpload);
        lastDownloadUse = HelperComputeInternt.internetUsedComputeFa(lastDownload);
        lastUploadUse = HelperComputeInternt.internetUsedComputeFa(lastUpload);
        mostUseInOne = HelperComputeInternt.internetUsedComputeFa(mostDownloadUseInOne) + " دانلود و " + HelperComputeInternt.internetUsedComputeFa(mostUploadUseUseInOne) + " آپلود";

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

        //****************************** Set Actions ******************************

        btnUseInternet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }


    //****************************** Internal Class Methods ******************************

    private void showDialog() {

        fromDate = "";
        toDate = "";
        fromClock = "";
        toClock = "";

        final Dialog dialog = new Dialog(ActivityMain.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_show_use_internet);

        Button btnFromDate = (Button) dialog.findViewById(R.id.btnFromDate);
        Button btnFromClock = (Button) dialog.findViewById(R.id.btnFromClock);
        Button btnToDate = (Button) dialog.findViewById(R.id.btnToDate);
        Button btnToClock = (Button) dialog.findViewById(R.id.btnToClock);
        Button btnShowUse = (Button) dialog.findViewById(R.id.btnShowUse);

        txtFromDate = (TextView) dialog.findViewById(R.id.txtFromDate);
        txtFromClock = (TextView) dialog.findViewById(R.id.txtFromClock);
        txtToDate = (TextView) dialog.findViewById(R.id.txtToDate);
        txtToClock = (TextView) dialog.findViewById(R.id.txtToClock);
        txtUseType = (TextView) dialog.findViewById(R.id.txtUseType);
        final TextView txtAmountUse = (TextView) dialog.findViewById(R.id.txtAmountUse);

        btnFromDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setDate("1");
            }
        });

        btnFromClock.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setDate("1");
            }
        });

        btnToDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setDate("2");
            }
        });

        btnToClock.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setClock("2");
            }
        });

        btnShowUse.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.i("LOG", "FromDate : " + fromDate + " 00:00:00");
                Log.i("LOG", "ToDate : " + toDate + " 00:00:00");

                int download = 0, upload = 0;

                Cursor cursor = G.cmd.selectInternetUsage(fromDate + fromClock, toDate + toClock);
                while (cursor.moveToNext()) {
                    Log.i("LOG", "Download : " + cursor.getInt(0));
                    download += cursor.getInt(0);
                    upload += cursor.getInt(1);
                }

                txtAmountUse.setText("*** Download : " + download + "*** \n *** Upload : " + upload + " ***");
            }
        });

        dialog.show();
    }


    private void setDate(final String value) {

        OnDateSetListener dateListener = new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int day) {

                String sDay = day + "";
                String sMonth = month + "";
                String sYear = year + "";
                String date;
                if (month < 10) {
                    sMonth = "0" + month;
                }
                if (day < 10) {
                    sDay = "0" + day;
                }
                //date = sYear + "-" + sMonth + "-" + sDay;
                date = sYear + sMonth + sDay;

                if (value.equals("1")) {

                    txtFromDate.setText(HelperComputeTime.formatDate(date));
                    fromDate = date;
                } else {
                    txtToDate.setText(HelperComputeTime.formatDate(date));
                    toDate = date;
                }

            }
        };

        DatePickerDialog dialog = new DatePickerDialog(ActivityMain.this, dateListener, G.calendar.get(Calendar.YEAR), G.calendar.get(Calendar.MONTH), G.calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setIcon(R.drawable.date);
        dialog.setTitle("تاریخ را انتخاب کنید");
        dialog.show();
    }


    private void setClock(final String value) {
        OnTimeSetListener timeListener = new OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker arg0, int hourOfDay, int minute) {

                String sHour = hourOfDay + "";
                String sMinute = minute + "";
                String clock;

                if (hourOfDay < 10) {
                    sHour = "0" + hourOfDay;
                }
                if (minute < 10) {
                    sMinute = "0" + minute;
                }

                clock = sHour + sMinute;

                if (value.equals("1")) {
                    txtFromClock.setText(HelperComputeTime.formatTime(clock));
                    fromClock = clock;
                } else {
                    txtToClock.setText(HelperComputeTime.formatTime(clock));
                    toClock = clock;
                }
            }
        };

        TimePickerDialog dialog = new TimePickerDialog(ActivityMain.this, timeListener, G.calendar.get(Calendar.HOUR_OF_DAY), G.calendar.get(Calendar.MINUTE), true);
        dialog.setIcon(R.drawable.clock);
        dialog.setTitle("زمان را انتخاب کنید");
        dialog.show();
    }

}
