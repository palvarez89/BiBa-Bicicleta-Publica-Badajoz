package biba.bicicleta.publica.badajoz.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import biba.bicicleta.publica.badajoz.BibaApp;
import biba.bicicleta.publica.badajoz.R;
import biba.bicicleta.publica.badajoz.objects.Estacion;
import biba.bicicleta.publica.badajoz.objects.EstacionList;
import biba.bicicleta.publica.badajoz.utils.Analytics;
import biba.bicicleta.publica.badajoz.utils.StationsRequest;

public class Map extends Fragment {

    private GoogleMap map = null;
    private Activity activity;
    private CameraPosition camerePosition;
    private BibaApp bibaApp;
    private final SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);
    FloatingActionButton fab;
    private Animation fab_refresh;


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

    private void performRequest(boolean force) {
        if (!force && bibaApp.estaciones != null) {
            updateMap(bibaApp.estaciones);
            return;
        }
        StationsRequest request = new StationsRequest();
        if (fab != null) {
            fab.startAnimation(fab_refresh);
        }
        spiceManager.execute(request, "cache", DurationInMillis.ONE_MINUTE, new EstacionListRequestListener());
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpMapIfNeeded();
        Analytics analytics = new Analytics(activity);
        analytics.screenView(this.getClass().getSimpleName());
        if (map != null) {
            fab = ((FloatingActionButton) getView().findViewById(R.id.fab));
            fab_refresh = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.fab_refresh);
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    performRequest(true);
                }
            });
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    private void setUpMapIfNeeded() {
        if (map == null) {
            map = ((SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            if (map != null) {
                map.setMyLocationEnabled(true);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                        38.874463, -6.974258), 12.0f));
                GoogleMapOptions options = new GoogleMapOptions();
                options.mapType(GoogleMap.MAP_TYPE_TERRAIN)
                        .compassEnabled(false).rotateGesturesEnabled(false)
                        .tiltGesturesEnabled(false).zoomControlsEnabled(true);
                performRequest(true);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.activity = (Activity) context;
        }
    }

    public void onPause() {
        super.onPause();
        if (map != null) {
            camerePosition = map.getCameraPosition();
            map = null;
        }
    }

    public void onResume() {
        super.onResume();
        fab = ((FloatingActionButton) getView().findViewById(R.id.fab));
        fab_refresh = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.fab_refresh);
        fab.startAnimation(fab_refresh);
        setUpMapIfNeeded();
        if (camerePosition != null) {
            map.moveCamera(CameraUpdateFactory.newCameraPosition(camerePosition));
            camerePosition = null;
        }
    }

    private void showPoint(Estacion station) {
        if (map != null) {
            map.addMarker(new MarkerOptions()
                    .position(
                            new LatLng(station.getLat(), station
                                    .getLon()))
                    .title(station.getName())
                    .snippet(
                            activity.getString(R.string.bikes) + ": " + station.getAvail() + " "
                                    + activity.getString(R.string.parkings) + ": " + station.getSpace()));
        }
    }

    private void updateMap(EstacionList estaciones) {
        if (isAdded()) {
            if (estaciones != null) {
                for (int i = 0; i < estaciones.size(); i++) {
                    showPoint(estaciones.get(i));
                }
            }
        }
    }

    private class EstacionListRequestListener implements RequestListener<EstacionList> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            fab.clearAnimation();
            Toast.makeText(activity.getApplicationContext(), R.string.failed_update,
                    Toast.LENGTH_LONG).show();
            updateMap(null);
        }

        @Override
        public void onRequestSuccess(EstacionList estaciones) {
            fab.clearAnimation();
            bibaApp.updateEstaciones(estaciones);
            updateMap(estaciones);
        }
    }

}
