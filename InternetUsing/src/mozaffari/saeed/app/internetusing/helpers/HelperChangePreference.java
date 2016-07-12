package mozaffari.saeed.app.internetusing.helpers;

import android.content.SharedPreferences;
import mozaffari.saeed.app.internetusing.tools.G;


public class HelperChangePreference {

    public static void changePreference(String key, int value) {
        SharedPreferences.Editor editor = G.preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    public static void changePreference(String key, String value) {
        SharedPreferences.Editor editor = G.preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public static void changePreference(String key, long value) {
        SharedPreferences.Editor editor = G.preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }


    public static void changePreference(String key, boolean value) {
        SharedPreferences.Editor editor = G.preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
