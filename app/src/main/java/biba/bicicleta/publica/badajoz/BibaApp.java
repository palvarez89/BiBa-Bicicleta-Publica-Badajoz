package biba.bicicleta.publica.badajoz;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import biba.bicicleta.publica.badajoz.objects.EstacionList;


public class BibaApp extends Application {


    private static final String PROPERTY_ID = "UA-4eeee1109565-1";
    public EstacionList estaciones= null;

    public synchronized Tracker getTracker() {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        return analytics.newTracker(PROPERTY_ID);
    }

    public void updateEstaciones(EstacionList estaciones){
        this.estaciones = estaciones;
    }


}
