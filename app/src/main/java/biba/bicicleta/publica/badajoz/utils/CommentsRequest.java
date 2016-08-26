package biba.bicicleta.publica.badajoz.utils;

import android.util.Log;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import java.util.Arrays;
import java.util.Collections;

import biba.bicicleta.publica.badajoz.objects.EstacionList;
import biba.bicicleta.publica.badajoz.objects.MessageList;

public class CommentsRequest extends SpringAndroidSpiceRequest<MessageList> {
    private int intento=0;
    private final String baseurl = "http://bibanotes.alvarezpiedehierro.com/";

    public CommentsRequest() {
        super(MessageList.class);
    }

    @Override
    public MessageList loadDataFromNetwork() throws Exception {
        int intentoActual = intento ++;
        String url = baseurl + "/list-comments/3";
        Log.w("CommentsRequest", url + " intento: " + intentoActual);
        return getRestTemplate().getForObject(url, MessageList.class);
    }
}