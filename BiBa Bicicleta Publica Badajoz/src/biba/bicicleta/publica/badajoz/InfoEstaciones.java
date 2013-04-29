package biba.bicicleta.publica.badajoz;

import java.text.DateFormat;
import java.util.Date;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class InfoEstaciones {

	private static InfoEstaciones infoEstaciones;
	String tag = "INFOESTACIONES";
	Vector<Estacion> Estaciones = null;

	String urls[] = { "http://biba.p.ht/3/getEstaciones.php",
			"http://biba2.hol.es/3/getEstaciones.php",
			"http://biba.webuda.com/3/getEstaciones.php" };

	String currentDateTimeString;

	private InfoEstaciones() {
		// Optional Code
	}

	public static InfoEstaciones getInstance() {
		if (infoEstaciones == null) {
			infoEstaciones = new InfoEstaciones();
		}
		return infoEstaciones;
	}

	public Vector<Estacion> getInfo(boolean force) throws JSONException {
		if (force || (Estaciones == null)) {
			Estaciones = readJSONBiba();
		}
		return Estaciones;

	}

	public String getDate() {
		return currentDateTimeString;
	}

	private Vector<Estacion> readJSONBiba() throws JSONException {

		for (int i = 0; i < urls.length; i++) {
			Log.w(tag, "Empezando " + (i + 1));
			JSONArray jArray = JSONManager.getJSONfromURL(urls[i]);
			if (jArray != null) {
				Log.w(tag, "RECIBIDO");
				currentDateTimeString = DateFormat.getDateTimeInstance()
						.format(new Date());
				return parseJSONBiba(jArray);

			} else {
				Log.w(tag, "Falla " + (i + 1));
			}
		}

		return null;
	}

	private Vector<Estacion> parseJSONBiba(JSONArray dataarray)
			throws JSONException {
		JSONObject jObject;
		int jNum, jDisp, jCap;
		float jLat, jLon;
		Estaciones = new Vector<Estacion>();
		String jNomb, jEstado;

		for (int i = 0; i < dataarray.length(); i++) {

			jObject = dataarray.getJSONObject(i);
			jNum = jObject.getInt("num");
			jNomb = jObject.getString("nombre");
			jEstado = jObject.getString("estado");
			jDisp = jObject.getInt("disp");
			jCap = jObject.getInt("cap");
			jLat = (float) jObject.getDouble("lat");
			jLon = (float) jObject.getDouble("long");

			Estaciones.add(new Estacion(jNum, jNomb, jEstado, jDisp, jCap,
					jLat, jLon));

		}
		return Estaciones;
	}
}
