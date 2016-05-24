package project.office.dictionary;

import java.util.ArrayList;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import project.office.dictionary.adapter.AdapterVisit;
import project.office.dictionary.tools.DrawerActivity;
import project.office.dictionary.tools.G;
import project.office.dictionary.tools.Struct;


public class VisitSort extends DrawerActivity {

    private ArrayAdapter      adapter;
    private ArrayList<Struct> structArray = new ArrayList<Struct>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
	//edit
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_sort);
        set();
        overridePendingTransition(R.anim.left_to_right_fade_in, R.anim.stay_view);

        ListView lstVisit = (ListView) findViewById(R.id.lstVisit);

        Cursor cursor = G.cmd.selectVisitSort();

        while (cursor.moveToNext()) {
            Struct struct = new Struct();
            struct.visitID = cursor.getInt(cursor.getColumnIndex("id"));
            struct.visitCount = cursor.getInt(cursor.getColumnIndex("visit"));
            struct.visitMeaning = cursor.getString(cursor.getColumnIndex("mean"));
            String word = cursor.getString(cursor.getColumnIndex("word"));
            struct.visitWord = word;
            String newWord = word.replaceAll("'", "''");
            struct.visitFav = G.cmd.selectIsFavorite(newWord);

            structArray.add(struct);
        }

        adapter = new AdapterVisit(structArray, VisitSort.this);
        lstVisit.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(G.context, StartupActivity.class);
        startActivity(intent);
        finish();
    }
}
