package biba.bicicleta.publica.badajoz;

import android.content.res.Configuration;
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
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biba_main);

        analytics = new Analytics(this);
        analytics.screenView(this.getClass().getSimpleName());

        // Set a toolbar to replace the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}


