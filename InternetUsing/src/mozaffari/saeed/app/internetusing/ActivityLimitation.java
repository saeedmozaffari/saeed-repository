package mozaffari.saeed.app.internetusing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import mozaffari.saeed.app.internetusing.helpers.HelperChangePreference;
import mozaffari.saeed.app.internetusing.tools.G;


public class ActivityLimitation extends ActionBarActivity {

    String daily;
    String monthly;
    String eachUse;
    String dataDaily;
    String dataMonthly;
    String dataEachUse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limitation);

        final EditText edtDaily = (EditText) findViewById(R.id.edtDaily);
        final EditText edtMonthly = (EditText) findViewById(R.id.edtMonthly);
        final EditText edtEachUse = (EditText) findViewById(R.id.edtEachUse);
        final EditText edtDataDaily = (EditText) findViewById(R.id.edtDataDaily);
        final EditText edtDataMonthly = (EditText) findViewById(R.id.edtDataMonthly);
        final EditText edtDataEachUse = (EditText) findViewById(R.id.edtDataEachUse);
        Button btnOk = (Button) findViewById(R.id.btnOk);

        getValue();

        edtDaily.setText(daily);
        edtMonthly.setText(monthly);
        edtEachUse.setText(eachUse);
        edtDataDaily.setText(dataDaily);
        edtDataMonthly.setText(dataMonthly);
        edtDataEachUse.setText(dataEachUse);

        btnOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                daily = edtDaily.getText().toString();
                monthly = edtMonthly.getText().toString();
                eachUse = edtEachUse.getText().toString();
                dataDaily = edtDataDaily.getText().toString();
                dataMonthly = edtDataMonthly.getText().toString();
                dataEachUse = edtDataEachUse.getText().toString();

                setValue();

                Intent intent = new Intent(G.context, ActivityMain.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void getValue() {
        daily = G.preferences.getInt("WIFI_DAILY", 0) + "";
        monthly = G.preferences.getInt("WIFI_MONTHLY", 0) + "";
        eachUse = G.preferences.getInt("WIFI_EACH_USE", 0) + "";

        dataDaily = G.preferences.getInt("DATA_DAILY", 0) + "";
        dataMonthly = G.preferences.getInt("DATA_MONTHLY", 0) + "";
        dataEachUse = G.preferences.getInt("DATA_EACH_USE", 0) + "";
    }


    private void setValue() {
        HelperChangePreference.changePreference("WIFI_DAILY", Integer.parseInt(daily));
        HelperChangePreference.changePreference("WIFI_MONTHLY", Integer.parseInt(monthly));
        HelperChangePreference.changePreference("WIFI_EACH_USE", Integer.parseInt(eachUse));

        HelperChangePreference.changePreference("DATA_DAILY", Integer.parseInt(dataDaily));
        HelperChangePreference.changePreference("DATA_MONTHLY", Integer.parseInt(dataMonthly));
        HelperChangePreference.changePreference("DATA_EACH_USE", Integer.parseInt(dataEachUse));
    }
}
