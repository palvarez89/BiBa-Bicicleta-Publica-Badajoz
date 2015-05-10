package biba.bicicleta.publica.badajoz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import biba.bicicleta.publica.badajoz.fragments.ListaEstaciones;
import biba.bicicleta.publica.badajoz.utils.Analytics;


public class BibaActivity extends ActionBarActivity {

    Analytics analytics;
    String deb = "DEBUG";
    Fragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biba_main);

        analytics = new Analytics(this);
        analytics.screenView("biba.bicicleta.publica.badajoz/BibaActivity");

        // Set a toolbar to replace the action bar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (savedInstanceState != null) {

            listFragment = getSupportFragmentManager().findFragmentById(
                    R.id.listaestaciones_f_container);

            if (listFragment == null) {
                Toast.makeText(this, "aaaaaqui", Toast.LENGTH_LONG).show();
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

        Log.w(deb, "BiBa Activity onCreate FRAGMENTED _LIST");

    }


}


