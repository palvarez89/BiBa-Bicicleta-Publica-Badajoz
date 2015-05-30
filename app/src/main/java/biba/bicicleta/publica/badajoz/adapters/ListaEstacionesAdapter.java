package biba.bicicleta.publica.badajoz.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import biba.bicicleta.publica.badajoz.R;
import biba.bicicleta.publica.badajoz.objects.EstacionList;
import biba.bicicleta.publica.badajoz.views.EstacionViewHolder;

public class ListaEstacionesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private EstacionList mListaEstaciones;

    public ListaEstacionesAdapter(EstacionList listaEstaciones) {
        mListaEstaciones = listaEstaciones;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.estacion_card, parent, false);
        return EstacionViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        EstacionViewHolder holder = (EstacionViewHolder) viewHolder;
        int numero = mListaEstaciones.get(position).getN();
        String nombre = toLowerCase(mListaEstaciones.get(position).getName());
        int bicis = mListaEstaciones.get(position).getAvail();
        int parkings = mListaEstaciones.get(position).getSpace();
        boolean estado = mListaEstaciones.get(position).getStateBool();
        holder.setEstacionInfo(numero, nombre, bicis, parkings, estado);
    }

    @Override
    public int getItemCount() {
        return mListaEstaciones == null ? 0 : mListaEstaciones.size();
    }

    public void replaceItems(EstacionList listaEstaciones) {
        mListaEstaciones = listaEstaciones;
    }

    private String toLowerCase(String str) {
        String[] words = str.split("\\s");
        String out = "";

        for (int i = 0; i < words.length - 1; i++) {
            out = out + toLowerCaseWord(words[i]) + " ";
        }
        out = out + toLowerCaseWord(words[words.length - 1]);
        return out;
    }

    private String toLowerCaseWord(String str) {

        if (str.length() == 0) return "";

        if (str.length() == 1) return str.toUpperCase();

        if (!Character.isLetter(str.charAt(0))) {
            return str.substring(0, 1) + str.substring(1, 2).toUpperCase() + str.substring(2).toLowerCase();
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        }
    }
}