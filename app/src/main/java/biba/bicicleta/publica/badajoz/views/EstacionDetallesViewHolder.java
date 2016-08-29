package biba.bicicleta.publica.badajoz.views;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Timestamp;

import biba.bicicleta.publica.badajoz.R;
import biba.bicicleta.publica.badajoz.objects.Message;


public class EstacionDetallesViewHolder extends RecyclerView.ViewHolder {
    protected TextView vMessage;
    protected TextView vTime;
    public ImageView vArchive;

    public EstacionDetallesViewHolder(View v) {
        super(v);
        vMessage = (TextView) v.findViewById(R.id.txtMessage);
        vTime = (TextView) v.findViewById(R.id.txtTime);
        vArchive = (ImageView) v.findViewById(R.id.imgArchive);
    }

    public void setDetails(Message msg) {
        vMessage.setText(msg.getMessage());
        Timestamp ts = msg.getTime();
        long currentTime = System.currentTimeMillis();
        String date = (String) DateUtils.getRelativeTimeSpanString(ts.getTime(), currentTime, DateUtils.MINUTE_IN_MILLIS);
        vTime.setText(date);
    }
}


