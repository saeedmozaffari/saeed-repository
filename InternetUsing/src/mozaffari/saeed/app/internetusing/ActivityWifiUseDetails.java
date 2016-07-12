package mozaffari.saeed.app.internetusing;

import java.util.ArrayList;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import mozaffari.saeed.app.internetusing.adapters.AdapterWifiUseDetails;
import mozaffari.saeed.app.internetusing.tools.G;
import mozaffari.saeed.app.internetusing.tools.StructInternet;


public class ActivityWifiUseDetails extends ActionBarActivity {

    private RecyclerView               recycler;
    private RecyclerView.Adapter       adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<StructInternet>  structs = new ArrayList<StructInternet>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_use_details);

        recycler = (RecyclerView) findViewById(R.id.recycler);

        Cursor cursor = G.cmd.select("*", "wifi_details ORDER BY download DESC");
        while (cursor.moveToNext()) {
            StructInternet struct = new StructInternet();

            struct.wifiName = cursor.getString(cursor.getColumnIndex("wifi_name"));
            struct.wifiDownload = cursor.getInt(cursor.getColumnIndex("download"));
            struct.wifiUpload = cursor.getInt(cursor.getColumnIndex("upload"));

            structs.add(struct);
        }
        cursor.close();

        layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        adapter = new AdapterWifiUseDetails(structs);
        recycler.setAdapter(adapter);
    }
}
