package mozaffari.saeed.app.internetusing;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import mozaffari.saeed.app.internetusing.helpers.HelperChangePreference;
import mozaffari.saeed.app.internetusing.helpers.HelperComputeTime;
import mozaffari.saeed.app.internetusing.tools.G;


public class ConnectivityChangedListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                if (hasActiveInternetConnection()) {
                    HelperChangePreference.changePreference("SHOW_LIMITATION_ALERT", true); // prepreation for show alert
                    HelperChangePreference.changePreference("START_MILLIS", System.currentTimeMillis());
                    HelperChangePreference.changePreference("START_DATE", HelperComputeTime.getCurrentDateNumerical());

                    Intent service = new Intent(G.context, WifiService.class);
                    G.context.startService(service);

                    G.runService = false;
                } else {
                    G.duration = System.currentTimeMillis() - G.preferences.getLong("START_MILLIS", 0);

                    if ( !G.runService) {
                        G.runService = true;
                        Intent service = new Intent(G.context, WifiService.class);
                        G.context.startService(service);
                    }
                }
            }
        });
        thread.start();
    }


    public boolean hasActiveInternetConnection() {

        ConnectivityManager conectivityManager = (ConnectivityManager) G.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conectivityManager.getActiveNetworkInfo();

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
}
