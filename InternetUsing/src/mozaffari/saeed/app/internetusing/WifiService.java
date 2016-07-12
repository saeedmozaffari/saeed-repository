package mozaffari.saeed.app.internetusing;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RemoteViews;
import mozaffari.saeed.app.internetusing.helpers.HelperChangePreference;
import mozaffari.saeed.app.internetusing.helpers.HelperComputeTime;
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

    private int                 id;
    private int                 beforDownload;
    private int                 beforUpload;

    private int                 daily;
    private int                 monthly;
    private int                 eachUse;
    private int                 dataDaily;
    private int                 dataMonthly;
    private int                 dataEachUse;

    private String              endLimitaionState;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                getLimitationValue();
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
            HelperChangePreference.changePreference("LAST_DOWNLOAD", (int) lastContentDownload);
            HelperChangePreference.changePreference("LAST_UPLOAD", (int) lastContentUpload);
            HelperChangePreference.changePreference("ALL_CONTENT", 1);
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

            HelperChangePreference.changePreference("ALL_CONTENT", 0);
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
                HelperChangePreference.changePreference("WIFI_NAME", getWifiName());
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

                        alertForUse();
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
                        HelperChangePreference.changePreference("TYPE", "wifi");
                    }
                }
                if (network.getType() == ConnectivityManager.TYPE_MOBILE) {
                    if (network.isConnected() && network.isAvailable()) {
                        Log.i("CONNECTION", "mobile data connected");
                        HelperChangePreference.changePreference("TYPE", "data");
                    }
                }
                if (network.getType() == ConnectivityManager.TYPE_ETHERNET) {
                    if (network.isConnected() && network.isAvailable()) {
                        Log.i("CONNECTION", "ethernet connected");
                        HelperChangePreference.changePreference("TYPE", "ethernet");
                    }
                }
                if (network.getType() == ConnectivityManager.TYPE_WIMAX) {
                    if (network.isConnected() && network.isAvailable()) {
                        Log.i("CONNECTION", "wimax connected");
                        HelperChangePreference.changePreference("TYPE", "wimax");
                    }
                }
                if (network.getType() == ConnectivityManager.TYPE_BLUETOOTH) {
                    if (network.isConnected() && network.isAvailable()) {
                        Log.i("CONNECTION", "bluetooth connected");
                        HelperChangePreference.changePreference("TYPE", "blutooth");
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

        String wifiName = "mobile_data_connection";
        String type = G.preferences.getString("TYPE", "");
        if (type.equals("wifi")) {
            wifiName = G.preferences.getString("WIFI_NAME", "");
        }
        int download = G.preferences.getInt("DOWNLOAD", 0);
        int upload = G.preferences.getInt("UPLOAD", 0);
        String dateStop = HelperComputeTime.getCurrentDateNumerical();
        String dateStart = G.preferences.getString("START_DATE", "");

        if (wifiName.equals("")) { // connection was not wifi
            G.cmd.insertUseDetails(wifiName, download, upload, dateStart, dateStop, G.duration);
        } else {
            G.cmd.insertUseDetails(wifiName, download, upload, dateStart, dateStop, G.duration);
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
            HelperChangePreference.changePreference("DOWNLOAD", (int) databaseDownload);
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
            HelperChangePreference.changePreference("UPLOAD", (int) databaseUpload);
            notificationUpload = databaseUpload + " Kb";
            if (databaseUpload > 1024) {
                uploadDivided = databaseUpload % 1024;
                uploadMB = databaseUpload / 1024;
                notificationUpload = uploadMB + " Mb ," + uploadDivided + " Kb";
            }
        }
    }


    private void alertForUse() {

        G.preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean showAlert = G.preferences.getBoolean("SHOW_LIMITATION_ALERT", true);

        Log.i("LOG", "showAlert : " + showAlert);
        Log.i("LOG", "uploadMB : " + uploadMB);
        Log.i("LOG", "daily : " + daily);

        if (showAlert) {

            //            endLimitaionState = "روزانه";
            //            dialog();

            if (daily != 0 && daily > downloadMB) {
                endLimitaionState = "روزانه";
            }
            if (monthly != 0 && monthly < downloadMB) {
                endLimitaionState = "ماهانه";
                dialog();
            }

            Log.i("LOG", "each use : " + eachUse + "  ||  Download : " + downloadMB);

            if (eachUse != 0 && eachUse < downloadMB) {
                endLimitaionState = "هر استفاده";
                dialog();
            }

            if (dataDaily != 0 && dataDaily < downloadMB) {
                endLimitaionState = "روزانه";
                dialog();
            }
            if (dataMonthly != 0 && dataMonthly < downloadMB) {
                endLimitaionState = "ماهانه";
                dialog();
            }
            if (dataEachUse != 0 && dataEachUse < downloadMB) {
                endLimitaionState = "هر استفاده";
                dialog();
            }
        } else {
            Log.i("LOG", "Can't show alert!");
        }
    }


    private void getLimitationValue() {
        Log.i("LOG", "getLimitationValue");
        daily = G.preferences.getInt("WIFI_DAILY", 0);
        monthly = G.preferences.getInt("WIFI_MONTHLY", 0);
        eachUse = G.preferences.getInt("WIFI_EACH_USE", 0);
        dataDaily = G.preferences.getInt("DATA_DAILY", 0);
        dataMonthly = G.preferences.getInt("DATA_MONTHLY", 0);
        dataEachUse = G.preferences.getInt("DATA_EACH_USE", 0);

        Log.i("LOG", "eachUse : " + eachUse);
    }


    private void dialog() {
        HelperChangePreference.changePreference("SHOW_LIMITATION_ALERT", false);
        /*
        G.handler.post(new Runnable() {
        
            @Override
            public void run() {
                Toast.makeText(G.context, "محدودیت " + endLimitaionState + " تعیین شده برای مصرف اینترنت به اتمام رسیده !" + "\n" + "آیا مایل به ادامه مصرف هستید ؟", Toast.LENGTH_LONG).show();
            }
        });
        */

        /*
        G.handler.post(new Runnable() {
            
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(G.context);
                builder.setMessage("محدودیت " + endLimitaionState + " تعیین شده برای مصرف اینترنت به اتمام رسیده !" + "\n" + "آیا مایل به ادامه مصرف هستید ؟");
                builder.setCancelable(true);
                
                builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                    
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                
                builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                    
                    public void onClick(DialogInterface dialog, int id) {
                        wifiState(false);
                        dialog.cancel();
                    }
                });
                
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        */

        G.handler.post(new Runnable() {

            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                final AlertDialog builder = new AlertDialog.Builder(G.context).create();
                builder.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                builder.setMessage("محدودیت " + endLimitaionState + " تعیین شده برای مصرف اینترنت به اتمام رسیده !" + "\n" + "آیا مایل به ادامه مصرف هستید ؟");
                builder.setCancelable(true);

                builder.setButton("بله", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        builder.cancel();
                    }
                });

                builder.setButton2("خیر", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        wifiState(false);
                        builder.cancel();
                    }
                });
                builder.show();
            }
        });

    }


    private void wifiState(boolean value) {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(value);
    }

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

}
