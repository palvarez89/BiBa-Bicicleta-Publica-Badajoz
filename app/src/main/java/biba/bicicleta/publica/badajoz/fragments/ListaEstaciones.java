package biba.bicicleta.publica.badajoz.fragments;

import java.util.Vector;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import biba.bicicleta.publica.badajoz.R;
import biba.bicicleta.publica.badajoz.adapters.ListaEstacionesAdapter;
import biba.bicicleta.publica.badajoz.objects.AsyncUpdateListaEstaciones;
import biba.bicicleta.publica.badajoz.objects.Estacion;
import biba.bicicleta.publica.badajoz.objects.InfoEstaciones;
import biba.bicicleta.publica.badajoz.utils.Analytics;
import biba.bicicleta.publica.badajoz.utils.GeneralSwipeRefreshLayout;

public class ListaEstaciones extends Fragment {

    Analytics analytics;

    Vector<Estacion> estaciones;
    InfoEstaciones infoEstaciones;

    Activity activity;
    RecyclerView recyclerView;

    ListaEstacionesAdapter adaptador = null;
    String deb = "DEBUG";

    private GeneralSwipeRefreshLayout swipeLayout;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        swipeLayout = (GeneralSwipeRefreshLayout) view.findViewById(
                R.id.activity_main_swipe_refresh_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        analytics = new Analytics(activity);
        analytics.screenView(this.getClass().getSimpleName());

        infoEstaciones = InfoEstaciones.getInstance();

        new AsyncUpdateListaEstaciones(activity, recyclerView, false, swipeLayout).execute();

        // Setup swipeLayout colors
        swipeLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);

        // Setup swipeLayout to play nice with thie ListView
        swipeLayout.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
            @Override
            public boolean canChildScrollUp() {
                LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                int firstVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                return firstVisiblePosition > 0;
            }
        });

        swipeLayout.setOnRefreshListener(new GeneralSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        new AsyncUpdateListaEstaciones(activity, recyclerView, true, swipeLayout).execute();
                    }
                });
            }
        });

        initRecyclerView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_estaciones, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    public class AsyncUpdateListaEstaciones
            extends biba.bicicleta.publica.badajoz.objects.AsyncUpdateListaEstaciones {

        private GeneralSwipeRefreshLayout swipeLayout;
        private RecyclerView recyclerView;

        public AsyncUpdateListaEstaciones(Activity activity, RecyclerView recyclerView, boolean forceUpdate, GeneralSwipeRefreshLayout swipeLayout) {
            super(activity, forceUpdate);
            this.recyclerView = recyclerView;
            this.swipeLayout = swipeLayout;
        }

        protected void onPostExecute(Vector<Estacion> result) {

            if (result == null) {
                Toast.makeText(activity.getApplicationContext(), R.string.failed_update,
                        Toast.LENGTH_LONG).show();
            } else {
                if (adaptador == null) {
                    adaptador = new ListaEstacionesAdapter(result);
                    recyclerView.setAdapter(adaptador);
                } else {
                    adaptador.replaceItems(result);
                }
                adaptador.notifyDataSetChanged();
            }

            swipeLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeLayout.setRefreshing(false);
                }
            });
        }


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