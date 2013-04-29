package biba.bicicleta.publica.badajoz;

import java.util.Vector;

import org.json.JSONException;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaGoogle extends SherlockFragmentActivity {
	GoogleMap map;
	InfoEstaciones infoEstaciones;
	Vector<Estacion> estaciones;
	BitmapDescriptor markerRed, markerGreen;
	Runnable readJSON;
	SherlockFragmentActivity activity;
	String deb = "DEBUG";

	TextView statusTV = null;
	int myProgress = 0;
	int col;
	String text = "";

	static boolean loaded = true;

	ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Log.w(deb, "MapaGoogle onCreate");
		activity = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapa_google);

		ActionBar ab = getSupportActionBar();
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE
				| ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
		ab.setTitle("Mapa BÌBa");

		infoEstaciones = InfoEstaciones.getInstance();

		setUpMapIfNeeded();

		progressBar = (ProgressBar) findViewById(R.id.progressbar_Horizontal_Map);
		statusTV = (TextView) findViewById(R.id.statusTVMap);
		new LoadJson().execute();

	}

	private void setUpMapIfNeeded() {
		Log.w(deb, "MapaGoogle setUpMapIfNeeded");
		if (map == null) {
			SupportMapFragment smf = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map2));
			if (smf != null) {
				map = smf.getMap();
				map.setMyLocationEnabled(true);

				map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
						38.874463, -6.974258), 12.0f));
				GoogleMapOptions options = new GoogleMapOptions();
				options.mapType(GoogleMap.MAP_TYPE_TERRAIN)
						.compassEnabled(false).rotateGesturesEnabled(false)
						.tiltGesturesEnabled(false).zoomControlsEnabled(true);

				// map:cameraTargetLat="38.874463"
				// map:cameraTargetLng="-6.974258"
				// map:cameraZoom="12"
				// map:uiTiltGestures="false"
				if (markerRed == null) {
					markerRed = BitmapDescriptorFactory
							.fromResource(R.drawable.redmark);
				}
				if (markerGreen == null) {
					markerGreen = BitmapDescriptorFactory
							.fromResource(R.drawable.greenmark);
				}
			}
		}

		// map:cameraTargetLat="38.874463"
		// map:cameraTargetLng="-6.974258"
		// map:cameraZoom="12"
		// map:ui+TiltGestures="false"
	}

	public void irHome() {
		finish();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.w(deb, "MapaGoogle onConfigurationChanged");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case android.R.id.home:
			irHome();
			return true;
		case R.id.update:
			loaded = false;
			new LoadJson().execute();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity_mapa_google, menu);
		return true;
	}

	public class LoadJson extends AsyncTask<Void, Integer, Vector<Estacion>> {

		int myProgress;

		@Override
		protected Vector<Estacion> doInBackground(Void... params) {

			// TODO Auto-generated method stub

			Thread thread = new Thread() {
				@Override
				public void run() {

					while (myProgress < 75) {
						myProgress++;
						publishProgress(myProgress);
						SystemClock.sleep(100);
					}

				}
			};

			thread.start();

			try {
				estaciones = new Vector<Estacion>();
				estaciones = infoEstaciones.getInfo(!loaded);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return estaciones;
		}

		@Override
		protected void onPreExecute() {
			if (map != null) {
				map.clear();
			}
			myProgress = 0;

			col = Color.parseColor("#FFD487");
			if (statusTV != null) {
				statusTV.setBackgroundColor(col);
				text = "Actualizando........";
				statusTV.setText(text);
			}
			if (progressBar != null) {
				progressBar.setBackgroundColor(col);
			}
		}

		@Override
		protected void onPostExecute(Vector<Estacion> result) {

			loaded = true;
			myProgress = 100;

			if (result != null) {
				publishProgress(myProgress);

				col = Color.parseColor("#A1FCA4");
				String currentDateTimeString = infoEstaciones.getDate();
				text = "Actualizado: " + currentDateTimeString;
				for (int i = 0; i < estaciones.size(); i++) {
					showPoint(estaciones.get(i));
				}

			} else {
				col = Color.parseColor("#FF8585");
				text = "Fallo al actualizar";
			}

			statusTV.setBackgroundColor(col);
			progressBar.setBackgroundColor(col);
			statusTV.setText(text);

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			progressBar.setProgress(values[0]);
		}

		private void showPoint(Estacion station) {
			if (map != null) {
				BitmapDescriptor marker;

				if (station.getEstado().indexOf("FUERA") != -1) {
					marker = markerRed;
				} else {
					marker = markerGreen;
				}
				map.addMarker(new MarkerOptions()
						.position(
								new LatLng(station.getLatitude(), station
										.getLongitude()))
						.title(station.getNombre())
						.icon(marker)
						.snippet(
								"Bicis: " + station.getDisponibles()
										+ " Parkings: " + station.getEspacio()));
			}
		}
	}

}