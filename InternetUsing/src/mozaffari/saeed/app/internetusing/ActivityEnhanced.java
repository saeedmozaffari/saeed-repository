package mozaffari.saeed.app.internetusing;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;
import mozaffari.saeed.app.internetusing.tools.G;


public class ActivityEnhanced extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.left_to_right_fade_in, R.anim.stay_view);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Toast.makeText(G.context, "OnBack", Toast.LENGTH_SHORT).show();
    }
}
