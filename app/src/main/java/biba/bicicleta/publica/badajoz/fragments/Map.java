package biba.bicicleta.publica.badajoz.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.octo.android.robospice.Jackson2SpringAndroidSpiceService;
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

public class Map extends Fragment implements OnMapReadyCallback {

    private final SpiceManager spiceManager = new SpiceManager(Jackson2SpringAndroidSpiceService.class);
    FloatingActionButton fab;
    private GoogleMap map = null;
    private Activity activity;
    private CameraPosition camerePosition;
    private BibaApp bibaApp;
    private Animation fab_refresh;


    @Override
    public void onStart() {
        bibaApp = (BibaApp) activity.getApplicationContext();
        super.onStart();
        spiceManager.start(activity);
        fab = ((FloatingActionButton) getView().findViewById(R.id.fab));
        fab_refresh = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.fab_refresh);

    }

    @Override
    public void onStop() {
        if (spiceManager.isStarted()) {
            spiceManager.shouldStop();
        }
        super.onStop();
    }

    private void performRequest(boolean force) {
        if (map == null) return;
        if (!force && bibaApp.estaciones != null) {
            updateMap(bibaApp.estaciones);
            return;
        }
        StationsRequest request = new StationsRequest();
        if (fab == null) {
            fab = ((FloatingActionButton) getView().findViewById(R.id.fab));
        }
        if (fab_refresh == null) {
            fab_refresh = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.fab_refresh);
        }
        fab.startAnimation(fab_refresh);

        spiceManager.execute(request, "cache", DurationInMillis.ONE_MINUTE, new EstacionListRequestListener());
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Analytics analytics = new Analytics(activity);
        analytics.screenView("Map.java");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    private void setUpMapIfNeeded() {
        if (map == null) {
            SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map));
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (googleMap != null) {

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                    38.874463, -6.974258), 12.0f));
            GoogleMapOptions options = new GoogleMapOptions();
            options.mapType(GoogleMap.MAP_TYPE_TERRAIN)
                    .compassEnabled(false).rotateGesturesEnabled(false)
                    .tiltGesturesEnabled(false).zoomControlsEnabled(true);
            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    performRequest(true);
                }
            });
            if (camerePosition != null) {
                map.moveCamera(CameraUpdateFactory.newCameraPosition(camerePosition));
                camerePosition = null;
            }
            performRequest(false);
        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
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
        if (map != null) {
            performRequest(false);
            if (camerePosition != null) {
                map.moveCamera(CameraUpdateFactory.newCameraPosition(camerePosition));
                camerePosition = null;
            }
        } else {
            setUpMapIfNeeded();
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

    final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (map != null) {
                            map.setMyLocationEnabled(true);
                        }
                    }
                }
            }
        }
    }
}
