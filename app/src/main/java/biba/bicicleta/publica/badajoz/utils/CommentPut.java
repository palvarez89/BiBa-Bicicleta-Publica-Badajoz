package biba.bicicleta.publica.badajoz.utils;

import android.util.Log;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import biba.bicicleta.publica.badajoz.objects.MessageList;

public class CommentPut extends SpringAndroidSpiceRequest<String> {
    private int intento=0;
    private final String baseurl = "http://bibanotes.alvarezpiedehierro.com";
    private int number;
    private String comment;

    public CommentPut(int number, String comment) {
        super(String.class);
        this.number = number;
        this.comment = comment;
    }

    @Override
    public String loadDataFromNetwork() throws Exception {
        int intentoActual = intento ++;
        String url = baseurl + "/add-comment/" + number;
        Log.w("CommentPut", url + " intento: " + intentoActual);

        Map<String, String> vars = new HashMap<>();
        vars.put("comment", comment);

        getRestTemplate().put(url, vars);
        return "OK";
    }
}