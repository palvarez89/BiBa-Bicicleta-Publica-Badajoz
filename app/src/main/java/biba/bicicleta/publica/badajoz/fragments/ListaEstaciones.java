package biba.bicicleta.publica.badajoz.fragments;

import java.util.Vector;

import org.json.JSONException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import biba.bicicleta.publica.badajoz.R;
import biba.bicicleta.publica.badajoz.adapters.ListaEstacionesAdapter;
import biba.bicicleta.publica.badajoz.objects.Estacion;
import biba.bicicleta.publica.badajoz.objects.InfoEstaciones;
import biba.bicicleta.publica.badajoz.utils.Analytics;
import biba.bicicleta.publica.badajoz.utils.GeneralSwipeRefreshLayout;

public class ListaEstaciones extends ListFragment {

    Analytics analytics;

    Vector<Estacion> estaciones;
    InfoEstaciones infoEstaciones;

    Activity activity;
    ListView listView;
    RecyclerView recyclerView;

    ListaEstacionesAdapter adaptador;
    String deb = "DEBUG";

    private GeneralSwipeRefreshLayout swipeLayout;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        View view = getView();
        listView = (ListView) view.findViewById(android.R.id.list);
        swipeLayout = (GeneralSwipeRefreshLayout) view.findViewById(
                R.id.activity_main_swipe_refresh_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);



        analytics = new Analytics(activity);
        analytics.screenView(this.getClass().getSimpleName());

        infoEstaciones = InfoEstaciones.getInstance();

        new AsyncUpdateListaEstaciones(activity, listView, false, swipeLayout).execute();

        // Setup swipeLayout colors
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
                        new AsyncUpdateListaEstaciones(activity, listView, true, swipeLayout).execute();
                    }
                });
            }
        });

        // Calculate ActionBar height and set offset for refreshing animation
        TypedValue tv = new TypedValue();
        int actionBarHeight=300;
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        swipeLayout.setProgressViewOffset(false, 0, actionBarHeight+150);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_estaciones, container, false);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(createItemList());
        recyclerView.setAdapter(recyclerAdapter);
    }

    public class AsyncUpdateListaEstaciones extends AsyncTask<Void, Integer, Vector<Estacion>> {

        Activity activity;
        ListView listView;
        GeneralSwipeRefreshLayout swipeLayout;
        boolean forceUpdate;

        public AsyncUpdateListaEstaciones(Activity activity, ListView listView, boolean forceUpdate,
                                          GeneralSwipeRefreshLayout swipeLayout) {
            this.activity = activity;
            this.listView = listView;
            this.forceUpdate = forceUpdate;
            this.swipeLayout = swipeLayout;
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
        protected void onPostExecute(Vector<Estacion> result) {

            if (result == null) {
                Toast.makeText(activity.getApplicationContext(), R.string.failed_update,
                        Toast.LENGTH_LONG).show();
            } else {
                adaptador = new ListaEstacionesAdapter(activity, result);
                listView.setAdapter(adaptador);
                adaptador.notifyDataSetChanged();
            }

            swipeLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeLayout.setRefreshing(false);
                }
            });
        }

        @Override
        protected void onPreExecute() {
            swipeLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeLayout.setRefreshing(true);
                }
            });
        }
    }
}