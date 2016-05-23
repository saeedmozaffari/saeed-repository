package project.office.dictionary;

import java.util.ArrayList;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import project.office.dictionary.adapter.AdapterUserWords;
import project.office.dictionary.tools.DrawerActivity;
import project.office.dictionary.tools.G;
import project.office.dictionary.tools.OnUserWordDeleteListener;
import project.office.dictionary.tools.Struct;


public class UserWords extends DrawerActivity {

    private ArrayAdapter                   adapter;
    private ArrayList<Struct>              structArray = new ArrayList<Struct>();
    private Cursor                         cursor;
    private ListView                       lst;
    public static OnUserWordDeleteListener onDeleteWordsListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_words);
        set();
        overridePendingTransition(R.anim.left_to_right_fade_in, R.anim.stay_view);

        final EditText edtWord = (EditText) findViewById(R.id.edtWord);
        final EditText edtMeaning = (EditText) findViewById(R.id.edtMeaning);
        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        lst = (ListView) findViewById(R.id.lstUserWords);

        edtWord.setTypeface(G.persianFont);
        edtMeaning.setTypeface(G.persianFont);

        btnAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String word = edtWord.getText().toString();
                String meaning = edtMeaning.getText().toString();

                if ( !word.equals("") && word != null) {
                    if ( !meaning.equals("") && meaning != null) {
                        G.cmd.insertUserWords("user_words", word, meaning);

                        edtWord.setText("");
                        edtMeaning.setText("");

                        Toast.makeText(G.context, "لغت شما با موفقیت درج شد", Toast.LENGTH_SHORT).show();

                        structArray.clear();
                        notifyListView();
                    } else {
                        Toast.makeText(G.context, "لطفا معنی را وارد نمایید", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(G.context, "لطفا لغت را وارد نمایید", Toast.LENGTH_SHORT).show();
                }
            }
        });

        onDeleteWordsListener = new OnUserWordDeleteListener() {

            @Override
            public void onDeleteWord() {
                structArray.clear();
                notifyListView();
            }
        };

        notifyListView();
    }


    private void notifyListView() {
        cursor = G.cmd.selectUserWords();

        Log.i("LOG", "Size : " + cursor.getCount());

        while (cursor.moveToNext()) {
            Struct struct = new Struct();
            struct.userWordID = cursor.getInt(cursor.getColumnIndex("id"));
            struct.userWord = cursor.getString(cursor.getColumnIndex("word"));
            struct.userMeaning = cursor.getString(cursor.getColumnIndex("mean"));

            structArray.add(struct);
        }

        adapter = new AdapterUserWords(structArray, UserWords.this);
        lst.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(G.context, StartupActivity.class);
        startActivity(intent);
        finish();
    }
}
