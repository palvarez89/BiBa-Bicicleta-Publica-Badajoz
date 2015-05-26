package biba.bicicleta.publica.badajoz.fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Vector;

import biba.bicicleta.publica.badajoz.R;
import biba.bicicleta.publica.badajoz.adapters.ListaEstacionesAdapter;
import biba.bicicleta.publica.badajoz.objects.Estacion;
import biba.bicicleta.publica.badajoz.utils.GeneralSwipeRefreshLayout;

public class Map extends Fragment {

    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap map = null;
    private Activity activity;
    private CameraPosition camerePosition;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpMapIfNeeded();
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
                new AsyncUpdateListaEstaciones(activity, false, map).execute();
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
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
        setUpMapIfNeeded();
        if (camerePosition != null) {
            map.moveCamera(CameraUpdateFactory.newCameraPosition(camerePosition));
            camerePosition = null;
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
    }

    public class AsyncUpdateListaEstaciones
            extends biba.bicicleta.publica.badajoz.objects.AsyncUpdateListaEstaciones {

        GoogleMap map;

        public AsyncUpdateListaEstaciones(Activity activity, boolean forceUpdate, GoogleMap map) {
            super(activity, forceUpdate);
            this.map = map;
        }

        protected void onPostExecute(Vector<Estacion> result) {

            if (result == null) {
                Toast.makeText(activity.getApplicationContext(), R.string.failed_update,
                        Toast.LENGTH_LONG).show();
            } else {
                for (int i = 0; i < result.size(); i++) {
                    showPoint(result.get(i));
                }
            }
        }

        @Override
        protected void onPreExecute() {
        }

        private void showPoint(Estacion station) {
            if (map != null) {
                BitmapDescriptor marker = BitmapDescriptorFactory.fromResource(R.drawable.bike);

//                if (station.getEstado().indexOf("FUERA") != -1) {
//                    marker = markerRed;
//                } else {
//                    marker = markerGreen;
//                }
                map.addMarker(new MarkerOptions()
                        .position(
                                new LatLng(station.getLatitude(), station
                                        .getLongitude()))
                        .title(station.getNombre())
//                        .icon(marker)
                        .snippet(
                                "Bicis: " + station.getDisponibles()
                                        + " Parkings: " + station.getEspacio()));
            }
        }

    }
}
