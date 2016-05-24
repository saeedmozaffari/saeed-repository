package project.office.dictionary.adapter;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import project.office.dictionary.R;
import project.office.dictionary.UserWords;
import project.office.dictionary.tools.G;
import project.office.dictionary.tools.Struct;


public class AdapterUserWords extends ArrayAdapter<Struct> {

    public static ContextWrapper contextWrapper = new ContextWrapper(G.context);
    public static AssetManager   asset;
    public static Typeface       typeface;
    public static Context        context;
    private int                  lastPosition;


    public AdapterUserWords(ArrayList<Struct> array, Context context) {
        super(G.context, R.layout.adapter_user_words, array);
        AdapterUserWords.context = context;
    }


    private class ViewHolder {

        public TextView     word;
        public TextView     meaning;
        public LinearLayout lyt;


        public ViewHolder(View view) {

            word = (TextView) view.findViewById(R.id.txtWord);
            meaning = (TextView) view.findViewById(R.id.txtMeaning);
            lyt = (LinearLayout) view.findViewById(R.id.lyt);
        }


        public void fill(final ArrayAdapter<Struct> adapter, final Struct item, final int position) {

            asset = contextWrapper.getAssets();
            typeface = Typeface.createFromAsset(asset, "primary.ttf");

            word.setText(item.userWord);
            meaning.setText(item.userMeaning);

            word.setTypeface(typeface);
            meaning.setTypeface(typeface);

            lyt.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View arg0) {

                    new AlertDialog.Builder(context).setMessage("آیا لغت مورد نظر حذف شود؟").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            G.cmd.deleteUserWords(item.userWordID);
                            UserWords.onDeleteWordsListener.onDeleteWord();
                            notifyDataSetChanged();
                        }
                    }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    }).show();
                    //.setIcon(android.R.drawable.ic_dialog_alert)
                    //.setTitle("Delete entry")

                    return false;
                }
            });

            /*
            float initialTranslation = (lastPosition <= position ? 700f : -700f);
            lyt.setTranslationY(initialTranslation);
            lyt.animate().setInterpolator(new DecelerateInterpolator(1.0f)).translationY(0f).setDuration(600l).setListener(null);
            lastPosition = position;
            */
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        Struct item = getItem(position);
        LayoutInflater inflater = (LayoutInflater) G.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_user_words, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.fill(this, item, position);

        return convertView;
    }

}
