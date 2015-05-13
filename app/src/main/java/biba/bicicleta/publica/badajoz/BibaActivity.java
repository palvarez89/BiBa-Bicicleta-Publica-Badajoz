package biba.bicicleta.publica.badajoz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import biba.bicicleta.publica.badajoz.fragments.ListaEstaciones;
import biba.bicicleta.publica.badajoz.utils.Analytics;


public class BibaActivity extends ActionBarActivity {

    Analytics analytics;
    Fragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biba_main);

        analytics = new Analytics(this);
        analytics.screenView(this.getClass().getSimpleName());

        initToolbar();
        initFragment(savedInstanceState);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
    }

    private void initFragment (Bundle savedInstanceState){
        if (savedInstanceState != null) {

            listFragment = getSupportFragmentManager().findFragmentById(
                    R.id.listaestaciones_f_container);

            if (listFragment == null) {
                listFragment = new ListaEstaciones();
                listFragment.setRetainInstance(true);
                FragmentTransaction transList = getSupportFragmentManager()
                        .beginTransaction();
                transList.add(R.id.listaestaciones_f_container, listFragment);
                transList.commit();
            }
            return;
        }

        listFragment = new ListaEstaciones();
        listFragment.setRetainInstance(true);
        FragmentTransaction transList = getSupportFragmentManager()
                .beginTransaction();
        transList.add(R.id.listaestaciones_f_container, listFragment);
        transList.commit();
    }
}


