package mozaffari.saeed.app.internetusing.adapters;

import java.util.ArrayList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import mozaffari.saeed.app.internetusing.R;
import mozaffari.saeed.app.internetusing.helpers.HelperComputeInternt;
import mozaffari.saeed.app.internetusing.helpers.HelperComputeTime;
import mozaffari.saeed.app.internetusing.tools.StructInternet;


public class AdapterAllUseDetails extends RecyclerView.Adapter<AdapterAllUseDetails.ViewHolder> {

    private ArrayList<StructInternet> structs;


    public AdapterAllUseDetails(ArrayList<StructInternet> value) {
        structs = value;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtDownload;
        public TextView txtUpload;
        public TextView txtTime;
        public TextView txtDate;


        public ViewHolder(View v) {
            super(v);
            txtDownload = (TextView) v.findViewById(R.id.txtDownload);
            txtUpload = (TextView) v.findViewById(R.id.txtUpload);
            txtTime = (TextView) v.findViewById(R.id.txtTime);
            txtDate = (TextView) v.findViewById(R.id.txtDate);
        }
    }


    @Override
    public void onBindViewHolder(final ViewHolder h, final int pos) {
        StructInternet item = structs.get(pos);

        //****************************** Preparation Values 

        String monthStart = HelperComputeTime.spliteAndformatDateTime(item.allDateStart)[1];
        String dayStart = HelperComputeTime.spliteAndformatDateTime(item.allDateStart)[2];

        String hourStart = HelperComputeTime.spliteAndformatDateTime(item.allDateStart)[3];
        String minuteStart = HelperComputeTime.spliteAndformatDateTime(item.allDateStart)[4];

        String hourStop = HelperComputeTime.spliteAndformatDateTime(item.allDateStop)[3];
        String minuteStop = HelperComputeTime.spliteAndformatDateTime(item.allDateStop)[4];

        String download = HelperComputeInternt.internetUsedComputeEn(item.allDownload);
        String upload = HelperComputeInternt.internetUsedComputeEn(item.allUpload);

        //****************************** Set Values 

        h.txtDownload.setText(download);
        h.txtUpload.setText(upload);
        h.txtDate.setText(monthStart + "/" + dayStart);
        h.txtTime.setText(hourStart + ":" + minuteStart + "_" + hourStop + ":" + minuteStop);
    }


    @Override
    public AdapterAllUseDetails.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_use_details, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public int getItemCount() {
        return structs.size();
    }

}
