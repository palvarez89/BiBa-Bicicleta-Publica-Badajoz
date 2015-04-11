package biba.bicicleta.publica.badajoz;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.Vector;

import biba.bicicleta.publica.badajoz.fragments.ActivityCommunicator;
import biba.bicicleta.publica.badajoz.fragments.ListaEstaciones;


public class MainActivity extends FragmentActivity implements
        ActivityCommunicator {

    String deb = "DEBUG";
    Fragment listFragment = null;
    boolean enFavs = false;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private CatNamesRecyclerViewAdapter mCatNamesRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listFragment = new ListaEstaciones(enFavs);

        listFragment.setRetainInstance(true);
        FragmentTransaction transList = getSupportFragmentManager().beginTransaction();
        transList.add(R.id.listaestaciones_f_container, listFragment);
        transList.commit();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
//        mRecyclerView = (RecyclerView) findViewById(R.id.activity_main_recyclerview);
//        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(layoutManager);
//        setupAdapter();

        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        setupAdapter();
                        update();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    // TODO: this should wait until refresh
                }, 2);
            }
        });
    }

    private void setupAdapter() {
        mCatNamesRecyclerViewAdapter = new CatNamesRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mCatNamesRecyclerViewAdapter);
    }


    public void passDataToActivity(Vector<Estacion> estaciones) {
        Log.w(deb, "BiBa Activity passDataToActivity");
//        if (map != null && estaciones != null) {
//
//            final Vector<Estacion> stations = estaciones;
//            Log.w(deb, "BiBa Activity hayMapa");
//
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    map.clear();
//                    for (int i = 0; i < stations.size(); i++) {
//
//                        showPoint(stations.get(i));
//
//                    }
//
//                }
//            });
//        }

    }

    public void update() {

        // if (listFragment == null) {
        // listFragment = new ListaEstaciones();
        // // ((ListaEstaciones) listFragment).newInstance();
        //
        // listFragment.setRetainInstance(true);
        // }

        ((ListaEstaciones) listFragment).update();
        // FragmentTransaction transaction = getSupportFragmentManager()
        // .beginTransaction();
        // transaction.detach(listFragment);
        // transaction.attach(listFragment);
        // transaction.commit();
        Log.w(deb, "BiBa Activity update ");
    }
}
