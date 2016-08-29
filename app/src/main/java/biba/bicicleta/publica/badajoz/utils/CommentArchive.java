package biba.bicicleta.publica.badajoz.utils;

import android.util.Log;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import biba.bicicleta.publica.badajoz.objects.MessageList;

public class CommentArchive extends SpringAndroidSpiceRequest<String> {
    private int intento=0;
    private final String baseurl = "http://bibanotes.alvarezpiedehierro.com";
    private int number;

    public CommentArchive(int number) {
        super(String.class);
        this.number = number;
    }

    @Override
    public String loadDataFromNetwork() throws Exception {
        int intentoActual = intento ++;
        String url = baseurl + "/archive-comment/" + number;
        Log.w("CommentsRequest", url + " intento: " + intentoActual);
        getRestTemplate().delete(url);
        return "OK";
    }
}