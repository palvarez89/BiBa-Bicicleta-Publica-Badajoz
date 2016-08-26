package biba.bicicleta.publica.badajoz.utils;

import android.util.Log;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import biba.bicicleta.publica.badajoz.objects.MessageList;

public class CommentsRequest extends SpringAndroidSpiceRequest<MessageList> {
    private int intento=0;
    private final String baseurl = "http://bibanotes.alvarezpiedehierro.com";
    private int number;

    public CommentsRequest(int number) {
        super(MessageList.class);
        this.number = number;
    }

    @Override
    public MessageList loadDataFromNetwork() throws Exception {
        int intentoActual = intento ++;
        String url = baseurl + "/list-comments/" + number;
        Log.w("CommentsRequest", url + " intento: " + intentoActual);
        return getRestTemplate().getForObject(url, MessageList.class);
    }
}