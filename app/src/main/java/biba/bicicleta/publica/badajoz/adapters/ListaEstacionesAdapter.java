package biba.bicicleta.publica.badajoz.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import biba.bicicleta.publica.badajoz.EstacionDetallesActivity;
import biba.bicicleta.publica.badajoz.R;
import biba.bicicleta.publica.badajoz.objects.Estacion;
import biba.bicicleta.publica.badajoz.objects.EstacionList;
import biba.bicicleta.publica.badajoz.utils.Common;
import biba.bicicleta.publica.badajoz.views.EstacionViewHolder;

public class ListaEstacionesAdapter extends RecyclerView.Adapter<EstacionViewHolder> {
    private EstacionList mListaEstaciones;
    private final SharedPreferences prefs;

    public ListaEstacionesAdapter(EstacionList listaEstaciones, SharedPreferences prefs) {
        mListaEstaciones = listaEstaciones;
        this.prefs = prefs;
    }

    public void filterFavs(boolean[] favList) {
        if (favList != null) {
            EstacionList el = new EstacionList();
            for (int i = 0; i < mListaEstaciones.size(); i++) {
                if (favList.length > i && favList[i]) {
                    el.add(mListaEstaciones.get(i));
                }
            }
            mListaEstaciones = el;
        }
    }

    @Override
    public EstacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.estacion_card, parent, false);
        return EstacionViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(EstacionViewHolder viewHolder, final int position) {

        final Estacion e = mListaEstaciones.get(position);
        final EstacionViewHolder holder = viewHolder;
        int numero = e.getN();
        String nombre = Common.toLowerCase(e.getName());
        int bicis = e.getAvail();
        int parkings = e.getSpace();
        boolean estado = e.getStateBool();
        holder.setEstacionInfo(numero, nombre, bicis, parkings, estado);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = holder.itemView.getContext();
                Intent detailsActivity = new Intent(context, EstacionDetallesActivity.class);
                detailsActivity.putExtra("estacion", e);
                context.startActivity(detailsActivity);
            }
        });
        final int realPosition = numero - 1;

        boolean isFav = prefs.getBoolean("fav" + realPosition, false);
        holder.setFavStar(isFav);

        viewHolder.mFavStar.setOnClickListener(new CardClickListener(isFav) {
            @Override
            public void onClick(View v) {
                isFav = !isFav;
                holder.setFavStar(isFav);
                prefs.edit().putBoolean("fav" + realPosition, isFav).apply();

            }

        });
    }

    @Override
    public int getItemCount() {
        return mListaEstaciones == null ? 0 : mListaEstaciones.size();
    }

    public void replaceItems(EstacionList listaEstaciones) {
        mListaEstaciones = listaEstaciones;
    }

    public abstract class CardClickListener implements View.OnClickListener {
        boolean isFav;

        public CardClickListener(boolean isFav) {
            this.isFav = isFav;
        }
    }
}