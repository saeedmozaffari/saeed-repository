package project.office.dictionary.tools;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import project.office.dictionary.R;


public class G extends Application {

    public static Context        context;
    public static SQLiteCommands cmd;
    public static LayoutInflater inflater;
    public static Animation      anim;
    public static Typeface       englishFont;
    public static Typeface       persianFont;
    public static CustomToast    toast;
    public static Handler        handler;
    public static int            offset = 0;
    public static boolean        persinaWord;


    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        anim = AnimationUtils.loadAnimation(this, R.anim.left_to_right_fade_in);
        handler = new Handler();
        englishFont = Typeface.createFromAsset(G.context.getAssets(), "primary.ttf");
        persianFont = Typeface.createFromAsset(G.context.getAssets(), "Yekan.ttf");
        toast = new CustomToast(context);
        cmd = new SQLiteCommands(context, 4);
        cmd.useable();
        cmd.open();

    }
}
