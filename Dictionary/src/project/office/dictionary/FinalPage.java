package project.office.dictionary;

import java.util.ArrayList;
import java.util.Locale;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import project.office.dictionary.tools.G;
import project.office.dictionary.tools.Struct;


public class FinalPage extends Activity {

    private int               visitNumber, wordID, favorite;
    private String            word, mean;
    private ImageView         imgFavorite, imgSpeaker, imgVisit;
    private int               onBackPress;
    private ArrayList<Struct> structs = new ArrayList<Struct>();
    private TextToSpeech      tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_page);
        //set();
        overridePendingTransition(R.anim.left_to_right_fade_in, R.anim.stay_view);

        //========================define elements
        TextView txtWord = (TextView) findViewById(R.id.txtWord);
        TextView txtMeaning = (TextView) findViewById(R.id.txtMeaning);
        TextView txtVisit = (TextView) findViewById(R.id.txtVisit);
        LinearLayout lytInfo = (LinearLayout) findViewById(R.id.lytInfo);
        imgSpeaker = (ImageView) findViewById(R.id.imgSpeaker);
        imgVisit = (ImageView) findViewById(R.id.imgVisit);
        imgFavorite = (ImageView) findViewById(R.id.imgFavorite);

        //=======================Set Typeface
        txtWord.setTypeface(G.englishFont);
        txtMeaning.setTypeface(G.englishFont);
        txtVisit.setTypeface(G.englishFont);

        //=======================Set Content
        Bundle extras = getIntent().getExtras();
        wordID = extras.getInt("ID");
        word = extras.getString("WORD");
        onBackPress = extras.getInt("ON_BACK");

        String newWord = word.replaceAll("'", "''");
        favorite = G.cmd.selectIsFavorite(newWord);

        if (favorite == 1) {
            imgFavorite.setImageResource(R.drawable.fav_after);
        } else {
            imgFavorite.setImageResource(R.drawable.fav_befor);
        }

        Cursor cursor = G.cmd.selectMeaning(wordID);
        while (cursor.moveToNext()) {
            //visitNumber = cursor.getInt(cursor.getColumnIndex("visit"));
            word = cursor.getString(cursor.getColumnIndex("word"));
            mean = cursor.getString(cursor.getColumnIndex("mean"));
            visitNumber = cursor.getInt(cursor.getColumnIndex("visit"));
        }
        cursor.close();

        visitNumber += 1;
        G.cmd.updateVisit(visitNumber, wordID);

        txtWord.setText(word);
        txtMeaning.setText(mean);
        txtVisit.setText("" + visitNumber);

        //=======================on Clicks
        txtVisit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                G.toast.Toast(R.string.visit);
            }
        });

        imgVisit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                G.toast.Toast(R.string.visit);
            }
        });

        imgFavorite.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (favorite == 1) {
                    String newWord = word.replaceAll("'", "''");
                    G.cmd.deleteFavorite(newWord);
                    imgFavorite.setImageResource(R.drawable.fav_befor);
                    favorite = 0;
                } else {
                    G.cmd.insertFavorite(word, mean, wordID);
                    imgFavorite.setImageResource(R.drawable.fav_after);
                    favorite = 1;
                }
            }
        });

        imgSpeaker.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tts = new TextToSpeech(G.context, new TextToSpeech.OnInitListener() {

                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            int result = tts.setLanguage(Locale.US);
                            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                Log.e("error", "This Language is not supported");
                                G.toast.Toast(R.string.toast_tts);
                                Intent installIntent = new Intent();
                                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                                startActivity(installIntent);
                            } else {
                                ConvertTextToSpeech();
                            }
                        } else {
                            Log.e("error", "Initilization Failed!");
                        }
                    }
                });
            }
        });

        /*
        visitNumber += 1;
        G.cmd.updateVisit(visitNumber, wordID);
        txtVisit.setText("" + visitNumber);
        
        
        Cursor infoCursor = G.cmd.selectInfo("meaning", wordID);
        
        while (infoCursor.moveToNext()) {
            Struct struct = new Struct();
            struct.finalLang = infoCursor.getString(infoCursor.getColumnIndex("lang"));
            struct.finalMeaning = infoCursor.getString(infoCursor.getColumnIndex("meaning"));
            struct.finalPos = infoCursor.getString(infoCursor.getColumnIndex("pos"));
            struct.finalPosOrg = infoCursor.getString(infoCursor.getColumnIndex("posOrg"));
            struct.finalIpa = infoCursor.getString(infoCursor.getColumnIndex("ipa"));
            struct.finalPron = infoCursor.getString(infoCursor.getColumnIndex("pron"));
            String data = infoCursor.getString(infoCursor.getColumnIndex("data"));
        
            JSONObject jsonResponse;
            try {
                jsonResponse = new JSONObject(data);
                JSONObject dataJson = jsonResponse.getJSONObject("data");
                struct.finalExample = dataJson.getString("example");
                struct.finalIdiom = dataJson.getString("idiom");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        
            structs.add(struct);
        }
        
        //Items
        for (int i = 0; i < infoCursor.getCount(); i++) {
            View view = G.inflater.inflate(R.layout.adapter_info, lytInfo, false);
        
            RelativeLayout lytMain = (RelativeLayout) view.findViewById(R.id.lytMain);
            LinearLayout lytExample = (LinearLayout) view.findViewById(R.id.lytExample);
            LinearLayout lytIdiom = (LinearLayout) view.findViewById(R.id.lytIdiom);
            TextView txtLang = (TextView) view.findViewById(R.id.txtLang);
            TextView txtMeaning = (TextView) view.findViewById(R.id.txtMeaning);
            TextView txtPos = (TextView) view.findViewById(R.id.txtPos);
            TextView txtIpa = (TextView) view.findViewById(R.id.txtIpa);
            TextView txtExampleDesc = (TextView) view.findViewById(R.id.txtExampleDesc);
            TextView txtIdiomDesc = (TextView) view.findViewById(R.id.txtIdiomDesc);
            TextView txtExample = (TextView) view.findViewById(R.id.txtExample);
            TextView txtIdiom = (TextView) view.findViewById(R.id.txtIdiom);
            final TextView txtPosOrg = (TextView) view.findViewById(R.id.txtPosOrg);
            final TextView txtPron = (TextView) view.findViewById(R.id.txtPron);
        
            lytMain.setVisibility(View.VISIBLE);
            //lytMain.startAnimation(G.anim);
        
            txtLang.setTypeface(G.englishFont);
            txtMeaning.setTypeface(G.englishFont);
            txtPos.setTypeface(G.englishFont);
            txtIpa.setTypeface(G.englishFont);
            txtExampleDesc.setTypeface(G.englishFont);
            txtIdiomDesc.setTypeface(G.englishFont);
            txtPosOrg.setTypeface(G.englishFont);
            txtPron.setTypeface(G.englishFont);
            txtExample.setTypeface(G.englishFont);
            txtIdiom.setTypeface(G.englishFont);
        
            txtLang.setText(structs.get(i).finalLang);
            txtMeaning.setText(structs.get(i).finalMeaning);
            txtPos.setText(structs.get(i).finalPos);
            txtIpa.setText(structs.get(i).finalIpa);
        
            if (structs.get(i).finalExample != null) {
                lytExample.setVisibility(View.VISIBLE);
                txtExampleDesc.setText(structs.get(i).finalExample);
            }
        
            if (structs.get(i).finalIdiom != null) {
                lytIdiom.setVisibility(View.VISIBLE);
                txtIdiomDesc.setText(structs.get(i).finalIdiom);
            }
        
            final int pos = i;
            txtPos.setOnClickListener(new OnClickListener() {
        
                @Override
                public void onClick(View arg0) {
        
                    if (structs.get(pos).finalPosOrg != null) {
                        txtPosOrg.setVisibility(View.VISIBLE);
                        txtPosOrg.startAnimation(G.anim);
                        txtPosOrg.setText(structs.get(pos).finalPosOrg);
                    }
                }
            });
        
            txtIpa.setOnClickListener(new OnClickListener() {
        
                @Override
                public void onClick(View arg0) {
                    if (structs.get(pos).finalPron != null) {
                        txtPron.setVisibility(View.VISIBLE);
                        txtPron.startAnimation(G.anim);
                        txtPron.setText(structs.get(pos).finalPron);
                    }
                }
            });
            lytInfo.addView(view);
        }
        
        //=======================on Clicks
        txtVisit.setOnClickListener(new OnClickListener() {
        
            @Override
            public void onClick(View arg0) {
                G.toast.Toast(R.string.visit);
            }
        });
        
        imgVisit.setOnClickListener(new OnClickListener() {
        
            @Override
            public void onClick(View arg0) {
                G.toast.Toast(R.string.visit);
            }
        });
        
        imgFavorite.setOnClickListener(new OnClickListener() {
        
            @Override
            public void onClick(View view) {
                favorite = G.cmd.selectIsFavorite("word", wordID);
                if (favorite == 1) {
                    G.cmd.updateFavorite(0, wordID);
                    imgFavorite.setImageResource(R.drawable.fav_befor);
                } else {
                    G.cmd.updateFavorite(1, wordID);
                    imgFavorite.setImageResource(R.drawable.fav_after);
                }
            }
        });
        
        imgSpeaker.setOnClickListener(new OnClickListener() {
        
            @Override
            public void onClick(View arg0) {
                tts = new TextToSpeech(G.context, new TextToSpeech.OnInitListener() {
        
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            int result = tts.setLanguage(Locale.US);
                            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                Log.e("error", "This Language is not supported");
                                G.toast.Toast(R.string.toast_tts);
                                Intent installIntent = new Intent();
                                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                                startActivity(installIntent);
                            } else {
                                ConvertTextToSpeech();
                            }
                        } else {
                            Log.e("error", "Initilization Failed!");
                        }
                    }
                });
            }
        });
        
        */

    }


    @SuppressWarnings("deprecation")
    private void ConvertTextToSpeech() {
        String text = word;
        if (text == null || "".equals(text)) {
            text = "Content not available";
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (onBackPress == 0) {
            Intent intent = new Intent(G.context, StartupActivity.class);
            startActivity(intent);
            finish();
        } else if (onBackPress == 1) {
            Intent intent = new Intent(G.context, Favorite.class);
            startActivity(intent);
            finish();
        } else if (onBackPress == 2) {
            Intent intent = new Intent(G.context, VisitSort.class);
            startActivity(intent);
            finish();
        }
    }
}
