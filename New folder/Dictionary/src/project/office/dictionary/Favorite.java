package project.office.dictionary;

import java.util.ArrayList;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import project.office.dictionary.adapter.AdapterFavorite;
import project.office.dictionary.tools.DrawerActivity;
import project.office.dictionary.tools.G;
import project.office.dictionary.tools.Struct;


public class Favorite extends DrawerActivity {

    private ArrayAdapter      adapter;
    private ArrayList<Struct> structArray = new ArrayList<Struct>();
    private Cursor            cursor;
    private ListView          lstFavorite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        set();
        overridePendingTransition(R.anim.left_to_right_fade_in, R.anim.stay_view);

        lstFavorite = (ListView) findViewById(R.id.lstFavorite);

        cursor = G.cmd.selectFavorite();
        notifyListView();
		//first comment
    }


    private void notifyListView() {
        while (cursor.moveToNext()) {
            Struct struct = new Struct();

            int id = cursor.getInt(cursor.getColumnIndex("word_id"));
            String word = cursor.getString(cursor.getColumnIndex("word"));
            struct.favID = id;
            struct.favWord = word;
            struct.favMeaning = cursor.getString(cursor.getColumnIndex("mean"));
            String newWord = word.replaceAll("'", "''");
            Cursor c = G.cmd.selectVisitNumber(newWord);
            while (c.moveToNext()) {
                struct.favVisit = c.getInt(c.getColumnIndex("visit"));
            }

            structArray.add(struct);
        }
//second comment
        adapter = new AdapterFavorite(structArray, Favorite.this);
        lstFavorite.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(G.context, StartupActivity.class);
        startActivity(intent);
        finish();
    }
	//third comment
}
