package biba.bicicleta.publica.badajoz.objects;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import biba.bicicleta.publica.badajoz.utils.JSONManager;

public class InfoEstaciones {

    private static InfoEstaciones infoEstaciones;
    String tag = "INFOESTACIONES";
    Vector<Estacion> Estaciones = null;

    String urls[] = { "http://biba2.hol.es/3/getEstaciones.php",
            "http://biba.w.pw/3/getEstaciones.php",
            "http://biba.webuda.com/3/getEstaciones.php" };

    String currentDateTimeString;

    private InfoEstaciones() {
        Collections.shuffle(Arrays.asList(urls));
    }

    public static InfoEstaciones getInstance() {
        if (infoEstaciones == null) {
            infoEstaciones = new InfoEstaciones();
        }
        return infoEstaciones;
    }

    public Vector<Estacion> getInfo(boolean force) throws JSONException {
        Vector<Estacion> NuevasEstaciones = Estaciones;
        if (force || (Estaciones == null)) {
            NuevasEstaciones = readJSONBiba();
            if (NuevasEstaciones != null) {
                Estaciones = NuevasEstaciones;
            }
        }
        return NuevasEstaciones;

    }

    public String getDate() {
        return currentDateTimeString;
    }

    private Vector<Estacion> readJSONBiba() throws JSONException {

        for (int i = 0; i < urls.length; i++) {
            Log.w(tag, "Empezando " + (i + 1) + " URL: " + urls[i]);
            JSONArray jArray = JSONManager.getJSONfromURL(urls[i]);
            if (jArray != null) {
                Log.w(tag, "RECIBIDO " + (i + 1) + " URL: " + urls[i]);
                currentDateTimeString = DateFormat.getDateTimeInstance()
                        .format(new Date());
                return parseJSONBiba(jArray);

            } else {
                Log.w(tag, "Falla " + (i + 1) + " URL: " + urls[i]);
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