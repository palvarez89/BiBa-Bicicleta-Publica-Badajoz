package biba.bicicleta.publica.badajoz.objects;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.Vector;

import biba.bicicleta.publica.badajoz.R;
import biba.bicicleta.publica.badajoz.adapters.ListaEstacionesAdapter;
import biba.bicicleta.publica.badajoz.utils.GeneralSwipeRefreshLayout;

public abstract class AsyncUpdateListaEstaciones extends AsyncTask<Void, Integer, Vector<Estacion>> {

    Activity activity;
    GeneralSwipeRefreshLayout swipeLayout;
    RecyclerView recyclerView;
    protected boolean forceUpdate;
    Vector<Estacion> estaciones;
    InfoEstaciones infoEstaciones;

    public AsyncUpdateListaEstaciones(Activity activity, boolean forceUpdate) {
        this.activity = activity;
        this.forceUpdate = forceUpdate;
        infoEstaciones = InfoEstaciones.getInstance();
    }

    @Override
    protected Vector<Estacion> doInBackground(Void... params) {
        try {
            estaciones = infoEstaciones.getInfo(forceUpdate);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return estaciones;
    }

    @Override
    abstract protected void onPostExecute(Vector<Estacion> result);

    @Override
    abstract protected void onPreExecute();
}