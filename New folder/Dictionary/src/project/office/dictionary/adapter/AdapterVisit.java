package project.office.dictionary.adapter;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import project.office.dictionary.FinalPage;
import project.office.dictionary.R;
import project.office.dictionary.tools.G;
import project.office.dictionary.tools.Struct;


public class AdapterVisit extends ArrayAdapter<Struct> {

    public static ContextWrapper contextWrapper = new ContextWrapper(G.context);
    public static AssetManager   asset;
    public static Typeface       typeface;
    public static Context        context;
    private int                  lastPosition;


    public AdapterVisit(ArrayList<Struct> array, Context context) {
        super(context, R.layout.adapter_favorite_and_visit, array);
        AdapterVisit.context = context;
    }


    private class ViewHolder {

        public TextView       txtWord;
        public TextView       txtVisit;
        public RelativeLayout lyt;
        public ImageView      imgFav;
        public ImageView      imgVisit;


        public ViewHolder(View view) {

            txtWord = (TextView) view.findViewById(R.id.txtWord);
            txtVisit = (TextView) view.findViewById(R.id.txtVisit);
            lyt = (RelativeLayout) view.findViewById(R.id.lyt);
            imgFav = (ImageView) view.findViewById(R.id.imgFav);
            imgVisit = (ImageView) view.findViewById(R.id.imgVisit);
        }


        public void fill(final ArrayAdapter<Struct> adapter, final Struct item, final int position) {

            asset = contextWrapper.getAssets();
            typeface = Typeface.createFromAsset(asset, "primary.ttf");

            txtWord.setText(item.visitWord);
            txtVisit.setText("" + item.visitCount);

            txtWord.setTypeface(typeface);
            txtVisit.setTypeface(typeface);

            if (item.visitFav == 1) {
                imgFav.setImageResource(R.drawable.fav_after);
            } else {
                imgFav.setImageResource(R.drawable.fav_befor);
            }

            imgFav.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    //int favorite = G.cmd.selectIsFavorite("word", item.visitID);
                    if (item.visitFav == 1) {
                        String newWord = item.visitWord.replaceAll("'", "''");
                        G.cmd.deleteFavorite(newWord);
                        imgFav.setImageResource(R.drawable.fav_befor);
                        item.visitFav = 0;
                    } else {
                        G.cmd.insertFavorite(item.visitWord, item.visitMeaning, item.visitID);
                        imgFav.setImageResource(R.drawable.fav_after);
                        item.visitFav = 1;
                    }
                }
            });

            lyt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(G.context, FinalPage.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("ID", item.visitID);
                    intent.putExtra("WORD", item.visitWord);
                    intent.putExtra("ON_BACK", 2);
                    G.context.startActivity(intent);
                    ((Activity) context).finish();
                }
            });

            float initialTranslation = (lastPosition <= position ? 700f : -700f);
            lyt.setTranslationY(initialTranslation);
            lyt.animate().setInterpolator(new DecelerateInterpolator(1.0f)).translationY(0f).setDuration(600l).setListener(null);
            lastPosition = position;
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        Struct item = getItem(position);
        LayoutInflater inflater = (LayoutInflater) G.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_favorite_and_visit, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.fill(this, item, position);

        return convertView;
    }

}
