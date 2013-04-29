package biba.bicicleta.publica.badajoz;

import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ArrayAdapter;
import biba.bicicleta.publica.badajoz.fragments.ActivityCommunicator;
import biba.bicicleta.publica.badajoz.fragments.ListaEstaciones;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class BiBaActivity extends SherlockFragmentActivity implements
		ActivityCommunicator {
	static int currentOption = 0;
	static Fragment listFragment = null;
	String deb = "DEBUG";
	GoogleMap map;
	BitmapDescriptor markerRed, markerGreen;
	boolean enFavs = false;

	String[] actions = new String[] { "TODAS", "FAVORITAS" };

	private void setUpMapIfNeeded() {
		if (map == null) {
			SupportMapFragment smf = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map));
			if (smf != null) {
				map = smf.getMap();
				map.setMyLocationEnabled(true);

				UiSettings settings = map.getUiSettings();
				settings.setRotateGesturesEnabled(false);
				settings.setTiltGesturesEnabled(false);

				map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
						38.874463, -6.974258), 12.0f));

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

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		Log.e(deb, "listFragment: " + (listFragment == null) + " Bundle: "
				+ (savedInstanceState == null));
		setupNavBar();
		Log.w(deb, "BiBa Activity onCreate ");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inicio);
		Log.w(deb, "BiBa Activity onCreate INFLATED");

		setUpMapIfNeeded();

		// no debe crear uno si tiene savedInstance, recuperar el antiguo, como?
		// if (listFragment == null) {
		// listFragment = new ListaEstaciones();
		//
		// listFragment.setRetainInstance(true);
		// FragmentTransaction transList = getSupportFragmentManager()
		// .beginTransaction();
		// transList.add(R.id.listaestaciones_f_container, listFragment);
		// transList.commit();
		// }
		// else {
		// getSupportFragmentManager().beginTransaction().remove(listFragment).commit();
		// listFragment = new ListaEstaciones();
		//
		// listFragment.setRetainInstance(true);
		// FragmentTransaction transList = getSupportFragmentManager()
		// .beginTransaction();
		// transList.add(R.id.listaestaciones_f_container, listFragment);
		// transList.commit();
		//
		// }
		//
		if (savedInstanceState != null) {
			return;
		}
		AppRater.app_launched(this);
		AppDonate.app_launched(this);

		Log.w(deb, "BiBa Activity onCreate FRAGMENTED _LIST");

		Log.w(deb, "BiBa Activity onCreate FRAGMENTED _MAP");

	}

	public void setupNavBar() {

		ActionBar ab = getSupportActionBar();
		ab.setIcon(R.drawable.actionbar_icon);
		ab.setTitle("");

		// ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
		// uuu

		/** Create an array adapter to populate dropdownlist */
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getBaseContext(), R.layout.sherlock_spinner_item, actions);

		/** Enabling dropdown list navigation for the action bar */
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		/** Defining Navigation listener */
		ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {

			public boolean onNavigationItemSelected(int itemPosition,
					long itemId) {
				// Toast.makeText(getBaseContext(),
				// "Current Action : " + actions[itemPosition],
				// Toast.LENGTH_SHORT).show();
				currentOption = itemPosition;

				switch (itemPosition) {
				case (0):
					((ListaEstaciones) listFragment).mostrarFavs(false);
					enFavs = false;
					break;
				case (1):
					((ListaEstaciones) listFragment).mostrarFavs(true);
					enFavs = true;
					break;
				}

				return false;
			}

		};

		/**
		 * Setting dropdown items and item navigation listener for the actionbar
		 */
		ab.setListNavigationCallbacks(adapter, navigationListener);

		ab.setSelectedNavigationItem(currentOption);
		adapter.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

		// iii
	}

	@Override
	public void onDestroy() {

		Log.w(deb, "Biba Activity onDestroy");

		super.onDestroy();
		Log.w(deb, "Biba Activity onDestroy2");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity_inicio, menu);
		Log.w(deb, "BiBa Activity onCreateOptionsMenu ");
		return true;
	}

	public void update() {

//		if (listFragment == null) {
//			listFragment = new ListaEstaciones();
//			// ((ListaEstaciones) listFragment).newInstance();
//
//			listFragment.setRetainInstance(true);
//		}

		((ListaEstaciones) listFragment).update();
		// FragmentTransaction transaction = getSupportFragmentManager()
		// .beginTransaction();
		// transaction.detach(listFragment);
		// transaction.attach(listFragment);
		// transaction.commit();
		Log.w(deb, "BiBa Activity update ");

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.ver_mapa:
			verMapa();
			return true;
		case R.id.instrucciones:
			instrucciones();
			return true;
		case R.id.update:
			update();
			return true;
		case R.id.donate:
			donate();
			return true;
		case R.id.share:
			share();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void share() {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = "Descarga ya la aplicacion Android de BiBa - https://play.google.com/store/apps/details?id=biba.bicicleta.publica.badajoz";
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"APP - Bicicleta Publica de Badajoz");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}

	private void verMapa() {

		Intent intentMain = new Intent(BiBaActivity.this, MapaGoogle.class);
		BiBaActivity.this.startActivity(intentMain);
	}

	private void instrucciones() {
		Intent intentMain = new Intent(BiBaActivity.this, InfoActivity.class);
		BiBaActivity.this.startActivity(intentMain);
	}

	private void donate() {
		AppDonate.openDonateVersion(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.w(deb, "BiBa Activity onPause");
		if (listFragment != null) {
			getSupportFragmentManager().beginTransaction().remove(listFragment)
					.commit();
			listFragment = null;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (listFragment == null) {
			listFragment = new ListaEstaciones(enFavs);

			 listFragment.setRetainInstance(true);
			FragmentTransaction transList = getSupportFragmentManager()
					.beginTransaction();
			transList.add(R.id.listaestaciones_f_container, listFragment);
			transList.commit();

//			((ListaEstaciones) listFragment).mostrarFavs(enFavs);
		}
		Log.w(deb, "BiBa Activity onResume");
	}

	@Override
	public void onStart() {
		super.onStart();

		//
		// if (listFragment == null) {
		// listFragment = new ListaEstaciones();
		//
		// // listFragment.setRetainInstance(true);
		// FragmentTransaction transList = getSupportFragmentManager()
		// .beginTransaction();
		// transList.add(R.id.listaestaciones_f_container, listFragment);
		// transList.commit();
		// } else {
		// getSupportFragmentManager().beginTransaction().remove(listFragment)
		// .commit();
		// listFragment = new ListaEstaciones();
		//
		// listFragment.setRetainInstance(true);
		// FragmentTransaction transList = getSupportFragmentManager()
		// .beginTransaction();
		// transList.add(R.id.listaestaciones_f_container, listFragment);
		// transList.commit();
		//
		// }
		Log.w(deb, "BiBa Activity onStart");
	}

	@Override
	public void onStop() {

		Log.w(deb, "BiBa Activity onStop");
		super.onStop();

	}

	//
	// @Override
	// public void onRestart() {
	// super.onRestart();
	//
	// Log.w(deb, "BiBa Activity onRestart");
	// }

	public void passDataToActivity(Vector<Estacion> estaciones) {
		Log.w(deb, "BiBa Activity passDataToActivity");
		if (map != null && estaciones != null) {
			
			final Vector<Estacion> stations = estaciones;
			Log.w(deb, "BiBa Activity hayMapa");

			runOnUiThread(new Runnable() {
				public void run() {
					map.clear();
					for (int i = 0; i < stations.size(); i++) {

						showPoint(stations.get(i));

					}

				}
			});
		}

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
