package biba.bicicleta.publica.badajoz.fragments;

import java.util.Vector;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import biba.bicicleta.publica.badajoz.Estacion;
import biba.bicicleta.publica.badajoz.InfoEstaciones;
import biba.bicicleta.publica.badajoz.MiAdaptador;
import biba.bicicleta.publica.badajoz.R;

import com.actionbarsherlock.app.SherlockListFragment;

public class ListaEstaciones extends SherlockListFragment {
	private ActivityCommunicator activityCommunicator;

	static String deb = "DEBUG";
	Vector<Estacion> estaciones;
	InfoEstaciones infoEstaciones;
	MiAdaptador adaptador;
	ProgressBar progressBar = null;
	TextView statusTV = null;
	int myProgress = 0;
	int col;
	String text = "";
	boolean mostrarFavs = false;

	boolean favsCargados = false;

	boolean favs[];

	boolean iniciado = true;

	public ListaEstaciones(boolean enfavs) {
		mostrarFavs = enfavs;
		Log.w(deb, "ListaEstaciones constructor");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		Log.w(deb, "ListaEstaciones onCreate ");
		super.onCreate(savedInstanceState);
		estaciones = new Vector<Estacion>();
		infoEstaciones = InfoEstaciones.getInstance();
		setRetainInstance(true);
		Log.w(deb, "ListaEstaciones onCreateEND ");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.w(deb, "ListaEstaciones onCreateView ");
		View v = inflater.inflate(R.layout.fragment_lista_estaciones,
				container, false);
		progressBar = (ProgressBar) v.findViewById(R.id.progressbar_Horizontal);
		statusTV = (TextView) v.findViewById(R.id.statusTV);
		Log.w(deb, "ListaEstaciones onCreateViewEND ");
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		new LoadJson().execute();
		iniciado = true;

		Log.w(deb, "ListaEstaciones onActivityCreated ");
		super.onActivityCreated(savedInstanceState);

		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				final int a = arg2;

				// /////////
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							// Yes button clicked
							if (!mostrarFavs) {
								addToFavs(a);
							}
							break;

						case DialogInterface.BUTTON_NEGATIVE:
							// No button clicked
							removeFromFavs(a);
							break;
						}
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setMessage("¿Mostrar en favoritos?")
						.setPositiveButton("Yes", dialogClickListener)
						.setNegativeButton("No", dialogClickListener).show();
				// /////////
				return true;
			}
		});

	}

	public void update() {
		iniciado = false;
		new LoadJson().execute();
	}

	public void addToFavs(int i) {
		cargarFavs(23);
		SharedPreferences prefs = getActivity().getSharedPreferences(
				"biba.bicicleta.publica.badajoz", Context.MODE_PRIVATE);
		prefs.edit().putBoolean("fav" + i, true).commit();
		favs[i] = true;
	}

	public void removeFromFavs(int i) {
		cargarFavs(23);
		boolean encontrado = false;
		int fixedpos = 0;

		if (mostrarFavs) {
			int pos = i;
			for (int j = 0; j < 23 && !encontrado; j++) {
				if (favs[j] == true) {
					if (pos == 0) {
						encontrado = true;
						fixedpos = j;
					} else {
						pos--;
					}
				}

			}
			SharedPreferences prefs = getActivity().getSharedPreferences(
					"biba.bicicleta.publica.badajoz", Context.MODE_PRIVATE);
			prefs.edit().putBoolean("fav" + fixedpos, false).commit();

			favs[fixedpos] = false;

			new LoadJson().execute();
		} else {
			SharedPreferences prefs = getActivity().getSharedPreferences(
					"biba.bicicleta.publica.badajoz", Context.MODE_PRIVATE);
			prefs.edit().putBoolean("fav" + i, false).commit();

			favs[i] = false;
		}

	}

	public void mostrarFavs(boolean swich) {
		mostrarFavs = swich;
		new LoadJson().execute();
	}

	private void cargarFavs(int result) {
		if (!favsCargados) {

			favs = new boolean[result];
			SharedPreferences prefs = getActivity().getSharedPreferences(
					"biba.bicicleta.publica.badajoz", Context.MODE_PRIVATE);

			for (int i = 0; i < result; i++) {
				favs[i] = prefs.getBoolean("fav" + i, false);
			}
			favsCargados = true;
		}

	}

	public void loadValues() {
		Log.w(deb, "ListaEstaciones loadValues ");
		if (progressBar != null) {
			progressBar.setProgress(myProgress);
			progressBar.setBackgroundColor(col);
		}
		if (statusTV != null) {
			statusTV.setText(text);
			statusTV.setBackgroundColor(col);
		}
	}

	public class LoadJson extends AsyncTask<Void, Integer, Vector<Estacion>> {

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
				estaciones = infoEstaciones.getInfo(!iniciado);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return estaciones;
		}

		@Override
		protected void onPreExecute() {
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

			myProgress = 100;

			if (result != null) {
				publishProgress(myProgress);
				if (mostrarFavs) {
					Vector<Estacion> resultfav = new Vector<Estacion>();

					// CargamosListaFavs
					cargarFavs(result.size());
					// CreamosListaFavs
					for (int i = 0; i < result.size(); i++) {
						if (favs[i]) {
							resultfav.add(result.get(i));
						}
					}
					adaptador = new MiAdaptador(getActivity(), resultfav);
				} else {
					adaptador = new MiAdaptador(getActivity(), result);
				}

				adaptador.notifyDataSetChanged();
				setListAdapter(adaptador);

				String currentDateTimeString = infoEstaciones.getDate();

				col = Color.parseColor("#A1FCA4");
				text = "Actualizado: " + currentDateTimeString;

			} else {
				col = Color.parseColor("#FF8585");
				text = "Fallo al actualizar";

			}

			if (statusTV != null) {
				statusTV.setBackgroundColor(col);
				statusTV.setText(text);
			}
			if (progressBar != null) {
				progressBar.setBackgroundColor(col);
			}
			// ///////////////TODO

			activityCommunicator.passDataToActivity(result);
			iniciado = true;

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			if (progressBar != null) {
				progressBar.setProgress(values[0]);
			}
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		Log.w(deb, "ListaEstaciones onPause");
	}

	@Override
	public void onResume() {
		super.onResume();

		Log.w(deb, "ListaEstaciones onResume");
	}

	@Override
	public void onStart() {
		super.onStart();

		Log.w(deb, "ListaEstaciones onStart");
	}

	@Override
	public void onStop() {
		super.onStop();

		Log.w(deb, "ListaEstaciones onStop");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		activityCommunicator = (ActivityCommunicator) getActivity();
	}

}
