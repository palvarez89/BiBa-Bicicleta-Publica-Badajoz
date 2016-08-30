package biba.bicicleta.publica.badajoz.utils;

import android.app.Activity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import biba.bicicleta.publica.badajoz.BibaApp;


public class Analytics {

    private final Activity activity;

    public Analytics (Activity activity){
        this.activity = activity;
    }
    public void screenView(String path){
        Tracker t = ((BibaApp) activity.getApplication()).getTracker();
        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName(path);

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

    }
}
