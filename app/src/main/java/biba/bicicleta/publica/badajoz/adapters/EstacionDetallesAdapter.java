package biba.bicicleta.publica.badajoz.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import biba.bicicleta.publica.badajoz.R;
import biba.bicicleta.publica.badajoz.objects.Message;
import biba.bicicleta.publica.badajoz.views.EstacionDetallesViewHolder;

public class EstacionDetallesAdapter extends RecyclerView.Adapter<EstacionDetallesViewHolder> {

    private List<Message> messageList;

    public EstacionDetallesAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public void onBindViewHolder(EstacionDetallesViewHolder estacionDetallesViewHolder, int i) {
        Message msg = messageList.get(i);
        estacionDetallesViewHolder.setDetails(msg.getMessage(), msg.getDate().toString());
    }

    @Override
    public EstacionDetallesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.estacion_details_card, viewGroup, false);

        return new EstacionDetallesViewHolder(itemView);
    }
}
