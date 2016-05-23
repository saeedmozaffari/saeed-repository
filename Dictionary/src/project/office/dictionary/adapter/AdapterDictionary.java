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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import project.office.dictionary.FinalPage;
import project.office.dictionary.R;
import project.office.dictionary.tools.G;
import project.office.dictionary.tools.Struct;


public class AdapterDictionary extends ArrayAdapter<Struct> {

    public static ContextWrapper contextWrapper = new ContextWrapper(G.context);
    public static AssetManager   asset;
    public static Typeface       typeface;
    public static Context        context;
    private int                  lastPosition;


    public AdapterDictionary(ArrayList<Struct> array, Context context) {
        super(G.context, R.layout.adapter_dictionary, array);
        AdapterDictionary.context = context;
    }


    private class ViewHolder {

        public TextView       word;
        public RelativeLayout lyt;
        public ImageView      imgVisit;


        public ViewHolder(View view) {

            word = (TextView) view.findViewById(R.id.txtWord);
            lyt = (RelativeLayout) view.findViewById(R.id.lyt);
            imgVisit = (ImageView) view.findViewById(R.id.imgVisit);
        }


        public void fill(final ArrayAdapter<Struct> adapter, final Struct item, final int position) {

            asset = contextWrapper.getAssets();
            typeface = Typeface.createFromAsset(asset, "primary.ttf");

            if (G.persinaWord) {
                word.setText(item.mean);
            } else {
                word.setText(item.word);
            }
            word.setTypeface(typeface);

            if (item.visit > 0) {
                imgVisit.setVisibility(View.VISIBLE);
            } else {
                imgVisit.setVisibility(View.INVISIBLE);
            }

            lyt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    G.offset = 0;

                    Intent intent = new Intent(G.context, FinalPage.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("ID", item.id);
                    intent.putExtra("WORD", item.word);
                    intent.putExtra("ON_BACK", 0);
                    G.context.startActivity(intent);
                    ((Activity) context).finish();
                }
            });

            /*
            float initialTranslation = (lastPosition <= position ? 700f : -700f);
            lyt.setTranslationY(initialTranslation);
            lyt.animate().setInterpolator(new DecelerateInterpolator(1.0f)).translationY(0f).setDuration(600l).setListener(null);
            lastPosition = position;
            */
            G.offset = position;
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        Struct item = getItem(position);
        LayoutInflater inflater = (LayoutInflater) G.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_dictionary, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.fill(this, item, position);

        return convertView;
    }

}
