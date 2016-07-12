package mozaffari.saeed.app.internetusing.adapters;

import java.util.ArrayList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import mozaffari.saeed.app.internetusing.R;
import mozaffari.saeed.app.internetusing.helpers.HelperComputeInternt;
import mozaffari.saeed.app.internetusing.tools.StructInternet;


public class AdapterWifiUseDetails extends RecyclerView.Adapter<AdapterWifiUseDetails.ViewHolder> {

    private ArrayList<StructInternet> structs;


    public AdapterWifiUseDetails(ArrayList<StructInternet> value) {
        structs = value;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtDownload;
        public TextView txtUpload;
        public TextView txtWifiName;


        public ViewHolder(View v) {
            super(v);
            txtDownload = (TextView) v.findViewById(R.id.txtDownload);
            txtUpload = (TextView) v.findViewById(R.id.txtUpload);
            txtWifiName = (TextView) v.findViewById(R.id.txtWifiName);
        }
    }


    @Override
    public void onBindViewHolder(final ViewHolder h, final int pos) {
        StructInternet item = structs.get(pos);

        //****************************** Preparation Values 

        String download = HelperComputeInternt.internetUsedComputeEn(item.wifiDownload);
        String upload = HelperComputeInternt.internetUsedComputeEn(item.wifiUpload);

        //****************************** Set Values 

        h.txtDownload.setText(download);
        h.txtUpload.setText(upload);
        h.txtWifiName.setText(item.wifiName);
    }


    @Override
    public AdapterWifiUseDetails.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi_use_details, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public int getItemCount() {
        return structs.size();
    }

}
