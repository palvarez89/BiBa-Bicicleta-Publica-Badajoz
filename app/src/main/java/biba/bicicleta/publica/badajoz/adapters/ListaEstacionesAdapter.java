package biba.bicicleta.publica.badajoz.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Vector;

import biba.bicicleta.publica.badajoz.R;
import biba.bicicleta.publica.badajoz.objects.Estacion;
import biba.bicicleta.publica.badajoz.views.EstacionViewHolder;

public class ListaEstacionesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Vector<Estacion> mListaEstaciones;

    public ListaEstacionesAdapter(Vector<Estacion> listaEstaciones) {
        mListaEstaciones = listaEstaciones;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.estacion_card, parent, false);
        return EstacionViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        EstacionViewHolder holder = (EstacionViewHolder) viewHolder;
        int numero = mListaEstaciones.get(position).getNumero();
        String nombre = mListaEstaciones.get(position).getNombre();
        int bicis = mListaEstaciones.get(position).getDisponibles();
        int parkings = mListaEstaciones.get(position).getEspacio();
        boolean estado = mListaEstaciones.get(position).getEstado();
        holder.setEstacionInfo(numero, nombre, bicis, parkings, estado);
    }

    @Override
    public int getItemCount() {
        return mListaEstaciones == null ? 0 : mListaEstaciones.size();
    }

    public void replaceItems(Vector<Estacion> listaEstaciones) {
        mListaEstaciones = listaEstaciones;
    }
}