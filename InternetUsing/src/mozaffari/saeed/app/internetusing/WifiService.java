package mozaffari.saeed.app.internetusing;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import mozaffari.saeed.app.internetusing.tools.G;


public class WifiService extends Service {

    private long                dataUpload;
    private long                uploadDivided;
    private long                oldDataUpload;
    private long                finalUpload;
    private long                uploadMB;
    private long                databaseUpload;
    private String              notificationUpload;

    private long                dataDownload;
    private long                downloadDivided;
    private long                oldDataDownload;
    private long                finalDownload;
    private long                downloadMB;
    private long                databaseDownload;
    private String              notificationDownload;

    private long                lastContentDownload;
    private long                lastContentUpload;

    private ConnectivityManager conectivityManager;
    private NetworkInfo         networkInfo;

    boolean                     connect;
    boolean                     connecting;

    private String              wifiName = "";

    private int                 id;
    private int                 beforDownload;
    private int                 beforUpload;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                createNotification();
            }
        });
        thread.start();

        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void createNotification() {

        int used = G.preferences.getInt("ALL_CONTENT", 0);
        if (used == 0) {

            lastContentDownload = TrafficStats.getTotalRxBytes();
            lastContentUpload = TrafficStats.getTotalTxBytes();
            //Log.i("LOG", "firstContent : " + TrafficStats.getTotalRxBytes() / 1024);
            changePreference("LAST_DOWNLOAD", (int) lastContentDownload);
            changePreference("LAST_UPLOAD", (int) lastContentUpload);
            changePreference("ALL_CONTENT", 1);
        }

        conectivityManager = (ConnectivityManager) G.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = conectivityManager.getActiveNetworkInfo();

        final int NOTIFICATION_ID = 1;

        final NotificationManager notificationManager = (NotificationManager) G.context.getSystemService(Context.NOTIFICATION_SERVICE);
        @SuppressWarnings("deprecation") final Notification notification = new Notification(R.drawable.wifi_white, null, System.currentTimeMillis());
        final RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notif_layout);

        Intent service = new Intent(this, WifiService.class);
        PendingIntent notificationButton = PendingIntent.getService(this, 0, service, 0);

        notificationView.setOnClickPendingIntent(R.id.btnRefresh, notificationButton);

        Intent notificationIntent = new Intent(G.context, ActivityMain.class);
        final PendingIntent contentIntent = PendingIntent.getActivity(G.context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationView.setOnClickPendingIntent(R.id.lytNotification, contentIntent);

        notification.contentView = notificationView;
        notification.contentIntent = null;
        notification.flags |= Notification.FLAG_NO_CLEAR;

        if ( !hasActiveInternetConnection()) {

            //superToast(4000, "Connection Failed    \n\n" + "Last Download : " + notificationDownload + "\n\nLast Upload : " + notificationUpload + "   ", SuperToast.Animations.FLYIN);

            changePreference("ALL_CONTENT", 0);
            notificationManager.cancel(NOTIFICATION_ID);
            insertToDatabase();
            Intent servicea = new Intent(G.context, WifiService.class);
            G.context.stopService(servicea);

        } else {

            //============Select All befor download and upload

            Cursor cursorAllUses = G.cmd.selectAllUse();
            while (cursorAllUses.moveToNext()) {
                id = cursorAllUses.getInt(cursorAllUses.getColumnIndex("id"));
                beforDownload = cursorAllUses.getInt(cursorAllUses.getColumnIndex("download"));
                beforUpload = cursorAllUses.getInt(cursorAllUses.getColumnIndex("upload"));
            }
            cursorAllUses.close();

            detectNetworkType();
            String type = G.preferences.getString("TYPE", "");

            if (type.equals("wifi")) {
                changeStringPreference("WIFI_NAME", getWifiName());
            }

            while (true) {
                if (networkInfo != null) {
                    connect = networkInfo.isConnected();
                    connecting = networkInfo.isConnectedOrConnecting();
                    if (connect || connecting) {
                        internetUsedCompute();

                        notificationView.setTextViewText(R.id.txtDownload, "Download : " + notificationDownload);
                        notificationView.setTextViewText(R.id.txtUpload, "Upload : " + notificationUpload);
                        notificationManager.notify(NOTIFICATION_ID, notification);

                        //alertForUse();
                    }
                }
                try {
                    Thread.sleep(10 * 1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void detectNetworkType() {
        NetworkInfo[] infoAvailableNetworks = conectivityManager.getAllNetworkInfo();
        if (infoAvailableNetworks != null) {
            for (NetworkInfo network: infoAvailableNetworks) {

                if (network.getType() == ConnectivityManager.TYPE_WIFI) {
                    if (network.isConnected() && network.isAvailable()) {
                        Log.i("CONNECTION", "wifi connected");
                        changeStringPreference("TYPE", "wifi");
                    }
                }
                if (network.getType() == ConnectivityManager.TYPE_MOBILE) {
                    if (network.isConnected() && network.isAvailable()) {
                        Log.i("CONNECTION", "mobile data connected");
                        changeStringPreference("TYPE", "data");
                    }
                }
                if (network.getType() == ConnectivityManager.TYPE_ETHERNET) {
                    if (network.isConnected() && network.isAvailable()) {
                        Log.i("CONNECTION", "ethernet connected");
                        changeStringPreference("TYPE", "ethernet");
                    }
                }
                if (network.getType() == ConnectivityManager.TYPE_WIMAX) {
                    if (network.isConnected() && network.isAvailable()) {
                        Log.i("CONNECTION", "wimax connected");
                        changeStringPreference("TYPE", "wimax");
                    }
                }
                if (network.getType() == ConnectivityManager.TYPE_BLUETOOTH) {
                    if (network.isConnected() && network.isAvailable()) {
                        Log.i("CONNECTION", "bluetooth connected");
                        changeStringPreference("TYPE", "blutooth");
                    }
                }
            }
        }
    }


    public boolean hasActiveInternetConnection() {
        if (networkInfo != null) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                Log.i("LOGA", "Connection status : " + (urlc.getResponseCode() == 200));
                return (urlc.getResponseCode() == 200);
            }
            catch (IOException e) {
                Log.e("LOG", "Error checking internet connection", e);
            }
        } else {
            Log.d("LOGA", "No network available!");
        }
        return false;
    }


    private void insertToDatabase() {

        Log.i("LOG", "insert");
        //yyyy-MM-dd HH:mm:ss ==> date insert format
        int year = G.calendar.get(Calendar.YEAR);
        int month = G.calendar.get(Calendar.MONTH);
        int day = G.calendar.get(Calendar.DAY_OF_MONTH);
        int hour = G.calendar.get(Calendar.HOUR_OF_DAY);
        int minute = G.calendar.get(Calendar.MINUTE);
        int second = G.calendar.get(Calendar.SECOND);

        String wifiName = "mobile_data_connection";
        String type = G.preferences.getString("TYPE", "");
        if (type.equals("wifi")) {
            wifiName = G.preferences.getString("WIFI_NAME", "");
        }
        int download = G.preferences.getInt("DOWNLOAD", 0);
        int upload = G.preferences.getInt("UPLOAD", 0);
        String date = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;

        if (wifiName.equals("")) { // connection was not wifi
            G.cmd.insertUseDetails(wifiName, download, upload, date, G.duration);
        } else {
            G.cmd.insertUseDetails(wifiName, download, upload, date, G.duration);

            Log.i("LOG", "wifiName : " + wifiName);
            Log.i("LOG", "download : " + download);
            Log.i("LOG", "upload : " + upload);
            Log.i("LOG", "date : " + date);
            Log.i("LOG", "duration : " + G.duration);
        }

        boolean wifiDetailsExist = G.cmd.isExist("wifi_details", " wifi_name = '" + wifiName + "'");
        if (wifiDetailsExist) { // update

            int beforDownload = 0, beforUpload = 0;

            Cursor cursorWifiDetails = G.cmd.select("*", "wifi_details", "wifi_name = '" + wifiName + "'");
            while (cursorWifiDetails.moveToNext()) {
                beforDownload = cursorWifiDetails.getInt(cursorWifiDetails.getColumnIndex("download"));
                beforUpload = cursorWifiDetails.getInt(cursorWifiDetails.getColumnIndex("upload"));
            }
            cursorWifiDetails.close();

            G.cmd.updateWifiDetails(wifiName, download + beforDownload, upload + beforUpload);
        } else { // insert
            G.cmd.insretWifiDetails(wifiName, download, upload);
        }

        //============plus and add to all_uses table
        if (G.cmd.selectAllUseCount("all_uses") == 0) {
            G.cmd.insertAllUses(download, upload);
        } else {
            G.cmd.updateAllUses(id, beforDownload + download, beforUpload + upload);
        }
    }


    private String getWifiName() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        return info.getSSID();
    }


    private void internetUsedCompute() {

        //========================Download Showing In Notification======================================================
        dataDownload = (TrafficStats.getTotalRxBytes() - G.preferences.getInt("LAST_DOWNLOAD", 0)) / 1024;

        finalDownload = dataDownload - oldDataDownload;

        if (finalDownload >= 0) {
            oldDataDownload = dataDownload;
            databaseDownload += finalDownload;
            changePreference("DOWNLOAD", (int) databaseDownload);
            notificationDownload = databaseDownload + " Kb";
            if (databaseDownload > 1024) {
                downloadDivided = databaseDownload % 1024;
                downloadMB = databaseDownload / 1024;
                notificationDownload = downloadMB + " Mb ," + downloadDivided + " Kb";
            }
        }

        //========================Uplaod Showing In Notification========================================================
        dataUpload = (TrafficStats.getTotalTxBytes() - G.preferences.getInt("LAST_UPLOAD", 0)) / 1024;

        finalUpload = dataUpload - oldDataUpload;

        if (finalUpload >= 0) {
            oldDataUpload = dataUpload;
            databaseUpload += finalUpload;
            changePreference("UPLOAD", (int) databaseUpload);
            notificationUpload = databaseUpload + " Kb";
            if (databaseUpload > 1024) {
                uploadDivided = databaseUpload % 1024;
                uploadMB = databaseUpload / 1024;
                notificationUpload = uploadMB + " Mb ," + uploadDivided + " Kb";
            }
        }
    }

    //    private void alertForUse() {
    //        if (downloadMB >= 1 & G.toast == 1) {
    //            superToast(2000, "10 MB Downloaded !", SuperToast.Animations.FADE);
    //            G.toast = 2;
    //        } else if (downloadMB >= 2 & G.toast == 2) {
    //            superToast(2000, "20 MB Downloaded !", SuperToast.Animations.FADE);
    //            G.toast = 10;
    //        }
    //    }

    //    private void superToast(final int time, final String text, final Animations animation) {
    //        G.handler.post(new Runnable() {
    //
    //            @Override
    //            public void run() {
    //                SuperToast superToast = new SuperToast(G.context);
    //                superToast.setDuration(time);
    //                superToast.setText(text);
    //                superToast.setAnimations(animation);
    //                //superToast.setTextColor(textColor);
    //                //superToast.setIcon(SuperToast.Icon.Dark.INFO, SuperToast.IconPosition.LEFT);
    //                superToast.setIcon(R.drawable.wifi_white, SuperToast.IconPosition.LEFT);
    //                superToast.show();
    //            }
    //        });
    //
    //    }


    private void changePreference(String key, int lastContent) {
        SharedPreferences.Editor editor = G.preferences.edit();
        editor.putInt(key, lastContent);
        editor.commit();
    }


    private void changeStringPreference(String key, String value) {
        SharedPreferences.Editor editor = G.preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

}
