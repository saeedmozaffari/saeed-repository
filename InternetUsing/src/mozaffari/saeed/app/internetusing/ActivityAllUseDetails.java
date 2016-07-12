package mozaffari.saeed.app.internetusing;

import java.util.ArrayList;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import mozaffari.saeed.app.internetusing.adapters.AdapterAllUseDetails;
import mozaffari.saeed.app.internetusing.tools.G;
import mozaffari.saeed.app.internetusing.tools.StructInternet;


public class ActivityAllUseDetails extends ActionBarActivity {

    private RecyclerView               recycler;
    private RecyclerView.Adapter       adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<StructInternet>  structs = new ArrayList<StructInternet>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_use_details);

        recycler = (RecyclerView) findViewById(R.id.recycler);

        Cursor cursor = G.cmd.select("*", "usage_details ORDER BY id DESC");
        while (cursor.moveToNext()) {
            StructInternet struct = new StructInternet();

            struct.allDownload = cursor.getInt(cursor.getColumnIndex("download"));
            struct.allUpload = cursor.getInt(cursor.getColumnIndex("upload"));
            struct.allDateStart = cursor.getString(cursor.getColumnIndex("use_date_start"));
            struct.allDateStop = cursor.getString(cursor.getColumnIndex("use_date_stop"));

            Log.i("LOG", "date : " + cursor.getString(cursor.getColumnIndex("use_date_start")));

            structs.add(struct);
        }
        cursor.close();

        layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);
        adapter = new AdapterAllUseDetails(structs);
        recycler.setAdapter(adapter);
    }
}
