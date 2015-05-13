package biba.bicicleta.publica.badajoz.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import biba.bicicleta.publica.badajoz.R;

public class EstacionViewHolder extends RecyclerView.ViewHolder {
    private final TextView mNumero;
    private final TextView mNombre;
    private final TextView mBicis;
    private final TextView mParkings;

    public EstacionViewHolder(final View parent, TextView numeroTextView, TextView nombreTextView,
                              TextView bicisTextView, TextView parkingsTextView) {
        super(parent);
        mNumero = numeroTextView;
        mNombre = nombreTextView;
        mBicis = bicisTextView;
        mParkings = parkingsTextView;
    }

    public static EstacionViewHolder newInstance(View parent) {
        TextView numeroTextView = (TextView) parent.findViewById(R.id.numero);
        TextView nombreTextView = (TextView) parent.findViewById(R.id.nombre);
        TextView bicisTextView = (TextView) parent.findViewById(R.id.bicis);
        TextView parkingsTextView = (TextView) parent.findViewById(R.id.parkings);
        return new EstacionViewHolder(parent, numeroTextView, nombreTextView, bicisTextView,
                parkingsTextView);
    }

    public void setEstacionInfo(int numero, CharSequence nombre, int bicis,
                                int parkings, boolean estado) {
        mNumero.setText(Integer.toString(numero));
        mNombre.setText(nombre);
        mBicis.setText(Integer.toString(bicis));
        mParkings.setText(Integer.toString(parkings));
    }

}