package biba.bicicleta.publica.badajoz.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import biba.bicicleta.publica.badajoz.R;


public class EstacionDetallesViewHolder extends RecyclerView.ViewHolder{
    protected TextView vMessage;
    protected TextView vTime;

    public EstacionDetallesViewHolder(View v) {
        super(v);
        vMessage =  (TextView) v.findViewById(R.id.txtMessage);
        vTime = (TextView)  v.findViewById(R.id.txtTime);
    }

    public void setDetails(String msg, String date) {
        vMessage.setText(msg);
        vTime.setText(date);
    }
}


