package biba.bicicleta.publica.badajoz;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;


public class BibaApp extends Application {


    private static final String PROPERTY_ID = "UA-41109565-1";

    public synchronized Tracker getTracker() {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        return analytics.newTracker(PROPERTY_ID);
    }

}
