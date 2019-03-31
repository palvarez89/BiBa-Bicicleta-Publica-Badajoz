package biba.bicicleta.publica.badajoz.utils;

import android.util.Log;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.Arrays;
import java.util.Collections;

import biba.bicicleta.publica.badajoz.objects.EstacionList;

public class StationsRequest extends SpringAndroidSpiceRequest<EstacionList> {
    private int intento=0;
    private final String[] urls = {
            "https://bibanotes.herokuapp.com/get-estaciones",
            // These are giving me problems
            // "http://biba4.epizy.com/4/getEstaciones.php", // https://app.infinityfree.net/accounts
            // "http://biba8.000webhostapp.com/4/getEstaciones.php", // https://www.000webhost.com/members/website/biba8/build
            // "http://biba7.000webhostapp.com/4/getEstaciones.php", // also 000webhost
            // not working php ini issues  https://cp1.awardspace.net/beta/
            // not working php ini issues http://palvarez89.5gbfree.com:2082
    };

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
