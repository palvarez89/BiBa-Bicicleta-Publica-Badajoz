package biba.bicicleta.publica.badajoz.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.octo.android.robospice.Jackson2SpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import biba.bicicleta.publica.badajoz.BibaApp;
import biba.bicicleta.publica.badajoz.R;
import biba.bicicleta.publica.badajoz.adapters.ListaEstacionesAdapter;
import biba.bicicleta.publica.badajoz.utils.Analytics;
import biba.bicicleta.publica.badajoz.objects.EstacionList;
import biba.bicicleta.publica.badajoz.utils.GeneralSwipeRefreshLayout;
import biba.bicicleta.publica.badajoz.utils.StationsRequest;

public class ListaEstaciones extends Fragment {

    private Activity activity;
    private RecyclerView recyclerView;
    private ListaEstacionesAdapter adaptador = null;
    private BibaApp bibaApp;
    boolean showFavs;
    private boolean[] favList = null;
    private GeneralSwipeRefreshLayout swipeLayout;

    private SharedPreferences prefs = null;
    private final SpiceManager spiceManager = new SpiceManager(Jackson2SpringAndroidSpiceService.class);


    public ListaEstaciones() {
        super();

    }

    @Override
    public void onStart() {
        bibaApp = (BibaApp) activity.getApplicationContext();
        super.onStart();
        spiceManager.start(activity);
        performRequest(false);
    }

    @Override
    public void onStop() {
        if (spiceManager.isStarted()) {
            spiceManager.shouldStop();
        }
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (swipeLayout != null) {
            swipeLayout.setRefreshing(false);
            swipeLayout.destroyDrawingCache();
            swipeLayout.clearAnimation();
        }
    }

    private void performRequest(boolean force) {
        if (!force && bibaApp.estaciones != null) {
            updateList(bibaApp.estaciones);
            return;
        }
        StationsRequest request = new StationsRequest();
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
            }
        });
        spiceManager.execute(request, "cache", DurationInMillis.ONE_MINUTE, new EstacionListRequestListener());
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        swipeLayout = (GeneralSwipeRefreshLayout) view.findViewById(
                R.id.activity_main_swipe_refresh_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        prefs = getActivity().getSharedPreferences(
                "biba.bicicleta.publica.badajoz", Context.MODE_PRIVATE);

        Analytics analytics = new Analytics(activity);
        analytics.screenView("ListaEstaciones.java");

        initSwipeLayout();
        initRecyclerView();
        initFavList();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_estaciones, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.activity = (Activity) context;
        }
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void initSwipeLayout() {
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
                        performRequest(true);
                    }
                });
            }
        });
    }

    private void initFavList() {
        if (showFavs && favList == null) {
            int MAX_FAVS = 40;
            favList = new boolean[MAX_FAVS];
            for (int i = 0; i < MAX_FAVS; i++) {
                favList[i] = prefs.getBoolean("fav" + i, false);
            }
        }
    }

    private void updateList(EstacionList estaciones) {
        if (isAdded()) {
            if (estaciones != null) {
                if (adaptador == null) {
                    adaptador = new ListaEstacionesAdapter(estaciones, prefs);
                    adaptador.filterFavs(favList);
                    recyclerView.setAdapter(adaptador);
                } else {
                    adaptador.replaceItems(estaciones);
                    adaptador.filterFavs(favList);
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
    }

    private class EstacionListRequestListener implements RequestListener<EstacionList> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(activity.getApplicationContext(), R.string.failed_update,
                    Toast.LENGTH_LONG).show();
            swipeLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeLayout.setRefreshing(false);
                }
            });
            updateList(null);
        }

        @Override
        public void onRequestSuccess(EstacionList estaciones) {
            bibaApp.updateEstaciones(estaciones);
            updateList(estaciones);
        }
    }
}