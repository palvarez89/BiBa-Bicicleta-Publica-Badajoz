package biba.bicicleta.publica.badajoz.utils;


import android.util.Log;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class StationsRequest extends SpringAndroidSpiceRequest<EstacionList> {
    int intento=0;
    String urls[] = { "http://biba2.hol.es/3/getEstaciones.php",
            "http://biba.w.pw/3/getEstaciones.php",
            "http://biba.webuda.com/3/getEstaciones.php" };

    public StationsRequest() {
        super(EstacionList.class);
    }

    @Override
    public EstacionList loadDataFromNetwork() throws Exception {
        EstacionList estacionList = null;
        Exception ex = null;
        boolean done = false;
        int intentoActual = intento ++;
        intentoActual = intentoActual % urls.length;
        Log.w("StationsRequest", urls[intentoActual] + " intento: " + intentoActual);
        return estacionList = getRestTemplate().getForObject(urls[intentoActual], EstacionList.class);
//        while (!done && i < urls.length) {
//            try {
//                Log.w("StationsRequest", urls[i] + intento);
//                estacionList = getRestTemplate().getForObject(urls[i], EstacionList.class);
//                done = true;
//            } catch (Exception e) {
//                Log.e("StationsRequest", urls[i] + " failed");
//                ex = e;
//                i++;
//            }
//        }
////        try {
////            Log.w("StationsRequest", urls[0]);
////            estacionList = getRestTemplate().getForObject(urls[0], EstacionList.class);
////        } catch (Exception e) {
////            Log.w("StationsRequest", urls[1]);
////            estacionList = getRestTemplate().getForObject(urls[1], EstacionList.class);
////        }
//        if (!done) {
//            throw new Exception("Error: " + ex.getMessage());
//        }
//        return estacionList;
    }
}