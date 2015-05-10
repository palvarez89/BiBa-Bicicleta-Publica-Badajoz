package biba.bicicleta.publica.badajoz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
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

        // Set a toolbar to replace the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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


