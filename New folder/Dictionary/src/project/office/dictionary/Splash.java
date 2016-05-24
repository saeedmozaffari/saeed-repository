package project.office.dictionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import project.office.dictionary.tools.G;


public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView txtWelcome = (TextView) findViewById(R.id.txtWelcom);

        txtWelcome.setTypeface(G.persianFont);
//onback press cleard
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                overridePendingTransition(R.anim.left_to_right_fade_in, R.anim.stay_view);

                Intent intent = new Intent(Splash.this, StartupActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }


    
}
