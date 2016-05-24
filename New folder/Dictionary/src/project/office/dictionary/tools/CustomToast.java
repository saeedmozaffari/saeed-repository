package project.office.dictionary.tools;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import project.office.dictionary.R;


public class CustomToast {

    Context mycontext;


    public CustomToast(Context context) {
        super();
        mycontext = context;
    }


    public void Toast(String matn) {

        LayoutInflater inflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast, null);
        TextView text = (TextView) view.findViewById(R.id.txtToast);
        text.setTypeface(G.persianFont);
        text.setText(matn);

        Toast toast = new Toast(mycontext);
        toast.setGravity(Gravity.BOTTOM, 0, 20);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }


    public void Toast(int matn) {

        LayoutInflater inflater = (LayoutInflater) mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast, null);
        TextView text = (TextView) view.findViewById(R.id.txtToast);
        text.setTypeface(G.persianFont);
        text.setText(matn);

        Toast toast = new Toast(mycontext);
        toast.setGravity(Gravity.BOTTOM, 0, 20);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }
}
