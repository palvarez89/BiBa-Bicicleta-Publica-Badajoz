package biba.bicicleta.publica.badajoz;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONException;

import java.util.Vector;

import biba.bicicleta.publica.badajoz.adapters.ListaEstacionesAdapter;
import biba.bicicleta.publica.badajoz.objects.Estacion;
import biba.bicicleta.publica.badajoz.objects.InfoEstaciones;
import biba.bicicleta.publica.badajoz.utils.GeneralSwipeRefreshLayout;


public class BibaActivity extends Activity {

    Vector<Estacion> estaciones;
    InfoEstaciones infoEstaciones;

    Activity activity;

    ListaEstacionesAdapter adaptador;
    String deb = "DEBUG";

    private GeneralSwipeRefreshLayout swipeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        estaciones = new Vector<Estacion>();
        infoEstaciones = InfoEstaciones.getInstance();

        final ListView listView;
        listView = (ListView) findViewById(R.id.list);

        new AsyncUpdateListaEstaciones(activity, listView).execute();


        // Setup swipeLayout colors
        swipeLayout = (GeneralSwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        swipeLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);

        // Setup swipeLayout to play nice with thie ListView
        swipeLayout.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
            @Override
            public boolean canChildScrollUp() {
                return listView.getFirstVisiblePosition() > 0 ||
                        listView.getChildAt(0) == null ||
                        listView.getChildAt(0).getTop() < 0;
            }
        });

        swipeLayout.setOnRefreshListener(new GeneralSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        new AsyncUpdateListaEstaciones(activity, listView).execute();
                    }
                });
            }
        });
    }

    public class AsyncUpdateListaEstaciones extends AsyncTask<Void, Integer, Vector<Estacion>> {

        Activity activity;
        ListView listView;
        GeneralSwipeRefreshLayout swipeLayout;

        public AsyncUpdateListaEstaciones(Activity activity, ListView listView){
            this.activity = activity;
            this.listView = listView;
            swipeLayout = (GeneralSwipeRefreshLayout) activity.findViewById(R.id.activity_main_swipe_refresh_layout);

        }

        @Override
        protected Vector<Estacion> doInBackground(Void... params) {
            Log.w(deb, "BiBa Activity doinbackround");
            try {
                estaciones = new Vector<Estacion>();
                estaciones = infoEstaciones.getInfo(true); // True to force refresh
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return estaciones;
        }

        @Override
        protected void onPostExecute(Vector<Estacion> result) {

            adaptador = new ListaEstacionesAdapter(activity, result);
            listView.setAdapter(adaptador);
            adaptador.notifyDataSetChanged();

            swipeLayout.setRefreshing(false);
        }

        @Override
        protected void onPreExecute(){

            swipeLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeLayout.setRefreshing(true);
                }
            });

        }
    }

}


