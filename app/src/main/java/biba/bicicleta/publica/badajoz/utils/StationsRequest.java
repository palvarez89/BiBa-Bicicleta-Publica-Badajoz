package biba.bicicleta.publica.badajoz.utils;

import android.util.Log;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.Arrays;
import java.util.Collections;

import biba.bicicleta.publica.badajoz.objects.EstacionList;

public class StationsRequest extends SpringAndroidSpiceRequest<EstacionList> {
    int intento=0;
    String urls[] = { "http://biba2.hol.es/4/getEstaciones.php",
            "http://biba.w.pw/4/getEstaciones.php",
            "http://biba.webuda.com/4/getEstaciones.php" };

    public StationsRequest() {
        super(EstacionList.class);
        Collections.shuffle(Arrays.asList(urls));
    }

    @Override
    public EstacionList loadDataFromNetwork() throws Exception {
        int intentoActual = intento ++;
        intentoActual = intentoActual % urls.length;
        Log.w("StationsRequest", urls[intentoActual] + " intento: " + intentoActual);
        return getRestTemplate().getForObject(urls[intentoActual], EstacionList.class);
    }
}
