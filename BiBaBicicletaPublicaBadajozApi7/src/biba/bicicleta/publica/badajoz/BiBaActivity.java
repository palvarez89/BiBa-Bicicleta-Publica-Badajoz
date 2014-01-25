package biba.bicicleta.publica.badajoz;

import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import biba.bicicleta.publica.badajoz.fragments.ActivityCommunicator;
import biba.bicicleta.publica.badajoz.fragments.ListaEstaciones;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class BiBaActivity extends SherlockFragmentActivity implements
		ActivityCommunicator {
	boolean iniciado = false;
	static int currentOption = 0;
	static Fragment listFragment = null;
	String deb = "DEBUG";
	
	boolean enFavs = false;

	String[] actions = new String[] { "TODAS", "FAVORITAS" };

	
	@Override
	public void onCreate(Bundle savedInstanceState) {

Log.w(deb, "BiBa Activity onCreate ");
		
		Log.e(deb, "listFragment: " + (listFragment == null) + " Bundle: "
				+ (savedInstanceState == null));
		if(listFragment == null){
			savedInstanceState=null;
		}
		super.onCreate(savedInstanceState);

		Log.w(deb, "BiBa Activity onCreate 2");
		setupNavBar();

		setContentView(R.layout.activity_inicio);
		Log.w(deb, "BiBa Activity onCreate INFLATED");

		

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

			listFragment = getSupportFragmentManager().findFragmentById(
					R.id.listaestaciones_f_container);

			if (listFragment == null) {
				Toast.makeText(this, "aaaaaqui", Toast.LENGTH_LONG).show();
				listFragment = new ListaEstaciones(enFavs);

				listFragment.setRetainInstance(true);
				FragmentTransaction transList = getSupportFragmentManager()
						.beginTransaction();
				transList.add(R.id.listaestaciones_f_container, listFragment);
				transList.commit();
			}
			return;
		}

		// if(listFragment==null){
		listFragment = new ListaEstaciones(enFavs);

		listFragment.setRetainInstance(true);
		FragmentTransaction transList = getSupportFragmentManager()
				.beginTransaction();
		transList.add(R.id.listaestaciones_f_container, listFragment);
		transList.commit();
		// }

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
		
	}

//	@Override
//	public void onResume() {
//	
//		
//		super.onResume();
//		if (iniciado == false) {
//			listFragment = new ListaEstaciones(enFavs);
//
//			 listFragment.setRetainInstance(true);
//			FragmentTransaction transList = getSupportFragmentManager()
//					.beginTransaction();
//			transList.add(R.id.listaestaciones_f_container, listFragment);
//			transList.commit();
//			iniciado = true;
//
////			((ListaEstaciones) listFragment).mostrarFavs(enFavs);
//		}
//		Log.w(deb, "BiBa Activity onResume");
//	}

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

	public void passDataToActivity(Vector<Estacion> estaciones) {
		// TODO Auto-generated method stub
		
	}

	//
	// @Override
	// public void onRestart() {
	// super.onRestart();
	//
	// Log.w(deb, "BiBa Activity onRestart");
	// }

}