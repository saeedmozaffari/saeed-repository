package mozaffari.saeed.app.internetusing.tools;

import java.util.Calendar;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import mozaffari.saeed.app.internetusing.R;


public class G extends Application {

    public static Context           context;
    public static SQLiteCommands    cmd;
    public static LayoutInflater    inflater;
    public static Animation         anim;
    public static Typeface          englishFont;
    public static Typeface          persianFont;
    public static Handler           handler;
    public static SharedPreferences preferences;
    public static Calendar          calendar;
    public static long              duration;
    public static boolean           runService = false;


    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        anim = AnimationUtils.loadAnimation(this, R.anim.left_to_right_fade_in);
        handler = new Handler();
        englishFont = Typeface.createFromAsset(G.context.getAssets(), "font/primary.ttf");
        persianFont = Typeface.createFromAsset(G.context.getAssets(), "font/persianfont.ttf");
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        calendar = Calendar.getInstance();
        cmd = new SQLiteCommands(context, 4);
        cmd.useable();
        cmd.open();

    }
}
