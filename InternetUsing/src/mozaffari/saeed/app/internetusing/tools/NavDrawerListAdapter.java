package mozaffari.saeed.app.internetusing.tools;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import mozaffari.saeed.app.internetusing.R;


public class NavDrawerListAdapter extends BaseAdapter {

    private Context                  context;
    private ArrayList<NavDrawerItem> navDrawerItems;
    public int[]                     drawerImages = { R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher };


    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }


    @Override
    public int getCount() {
        return navDrawerItems.size();
    }


    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_item, null);
        }

        TextView txtTitle = (TextView) convertView.findViewById(R.id.txt_drawername);
        ImageView imgDrawer = (ImageView) convertView.findViewById(R.id.imgDrawer);

        txtTitle.setText(navDrawerItems.get(position).getTitle());
        txtTitle.setTypeface(G.persianFont);

        imgDrawer.setImageResource(drawerImages[position]);

        return convertView;
    }

}
