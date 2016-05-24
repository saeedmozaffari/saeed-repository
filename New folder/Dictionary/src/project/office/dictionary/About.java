package project.office.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import project.office.dictionary.tools.DrawerActivity;
import project.office.dictionary.tools.G;


public class About extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        set();
        overridePendingTransition(R.anim.left_to_right_fade_in, R.anim.stay_view);

        TextView txtAbout = (TextView) findViewById(R.id.txtAbout);
        txtAbout.setTypeface(G.persianFont);
    }
	
	//new comment for test git


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(G.context, StartupActivity.class);
        startActivity(intent);
        finish();
    }
}
