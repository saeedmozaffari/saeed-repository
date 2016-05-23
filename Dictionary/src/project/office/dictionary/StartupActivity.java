package project.office.dictionary;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import project.office.dictionary.adapter.AdapterDictionary;
import project.office.dictionary.tools.DrawerActivity;
import project.office.dictionary.tools.G;
import project.office.dictionary.tools.Struct;


public class StartupActivity extends DrawerActivity {

    private ArrayAdapter      adapter;
    private ArrayList<Struct> structArray   = new ArrayList<Struct>();
    private Cursor            cursor;
    private ListView          lst;
    private long              mBackPressed;
    private static final int  TIME_INTERVAL = 2000;                   // # milliseconds time
    private int               lastItem;
    private int               previousLast;
    private boolean           search;
    private long              firstTime, secondTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        set();
        overridePendingTransition(R.anim.left_to_right_fade_in, R.anim.stay_view);

        Log.i("LOG_KIND", "search default value : " + search);

        final EditText edt = (EditText) findViewById(R.id.edt);
        lst = (ListView) findViewById(R.id.lst);

        lst.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {

            }


            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                String text = edt.getText().toString();

                if (text.equals("")) {
                    //                    if ( !search) { // beacause when i serach change item of listview and run this code and it is a problem
                    //                        lastItem = firstVisibleItem + visibleItemCount;
                    //                        if (lastItem == totalItemCount) {
                    //                            if (previousLast != lastItem) {
                    //                                previousLast = lastItem;
                    //                                cursor = G.cmd.selectAll();
                    //                                notifyListView();
                    //                                Log.i("LOG", "offset : " + G.offset);
                    //                                lst.setSelection(G.offset);
                    //                            }
                    //                        }
                    //                    } else {
                    //                        Log.i("LOG", "search : " + search);
                    //                        search = false;
                    //                    }
                    lastItem = firstVisibleItem + visibleItemCount;
                    if (lastItem == totalItemCount) {
                        if (previousLast != lastItem) {
                            previousLast = lastItem;
                            cursor = G.cmd.selectAll();
                            notifyListView();
                            Log.i("LOG", "offset : " + G.offset);
                            lst.setSelection(G.offset);
                        }
                    }
                }
            }
        });

        edt.setTypeface(G.persianFont);
        edt.addTextChangedListener(new TextWatcher() {

            @SuppressLint("NewApi")
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                char[] chars = edt.getText().toString().toCharArray();
                int lettersLenght = chars.length;
                if (lettersLenght != 0) {
                    int c = chars[(lettersLenght - 1)];
                    if (1544 < c && c < 1970) {//persian
                        G.persinaWord = true;
                    } else {// english
                        G.persinaWord = false;
                    }
                    Log.i("LOG_KIND", "persinaWord : " + G.persinaWord);
                }
            }


            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}


            @Override
            public void afterTextChanged(Editable arg0) {
                firstTime = System.currentTimeMillis();
                Log.i("LOG_TIME", "Time 1 : " + firstTime);
                search = true;

                if (edt.getText().toString() != null && !edt.getText().toString().isEmpty() && !edt.getText().toString().equals("")) {
                    if (G.persinaWord) {
                        cursor = G.cmd.selectPersianWords("dictionary", edt.getText().toString());
                    } else {
                        cursor = G.cmd.selectEnglishWords("dictionary", edt.getText().toString());
                    }
                } else {
                    G.offset = 0;
                    cursor = G.cmd.selectAll();
                }
                structArray.clear();
                notifyListView();
            }
        });

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                G.handler.post(new Runnable() {

                    @Override
                    public void run() {
                        cursor = G.cmd.selectAll();
                        notifyListView();
                    }
                });
            }
        });
        thread.start();

    }


    private void notifyListView() {
        while (cursor.moveToNext()) {
            Struct struct = new Struct();
            struct.id = cursor.getInt(cursor.getColumnIndex("id"));
            struct.word = cursor.getString(cursor.getColumnIndex("word"));
            struct.mean = cursor.getString(cursor.getColumnIndex("mean"));

            Log.i("LOG", "Word : " + struct.word + "  ||  Mean : " + struct.mean);

            structArray.add(struct);
        }
        secondTime = System.currentTimeMillis();
        Log.i("LOG_TIME", "Time 2 : " + secondTime);
        long delayTime = (secondTime - firstTime);
        Log.i("LOG_TIME", "Time Delay milli second : " + delayTime);
        Log.i("LOG_TIME", "Time Delay second : " + (delayTime / 1000));
        Log.i("LOG_TIME", "Time Delay minute : " + ((delayTime / 1000) / 60));

        adapter = new AdapterDictionary(structArray, StartupActivity.this);
        lst.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            G.toast.Toast(R.string.toast_exit);
        }

        mBackPressed = System.currentTimeMillis();
    }

}
