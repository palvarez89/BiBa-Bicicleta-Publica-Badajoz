package biba.bicicleta.publica.badajoz.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.sql.Timestamp;

public class Message {


    @JsonProperty("id")
    private int id;

    @JsonProperty("estacion_id")
    private int estacionId;

    @JsonProperty("comentario")
    private String message;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty("escrito_en")
    private Timestamp time;

    public Message() {
    }

    public Message(String msg, Timestamp time) {
        this.message = msg;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEstacionId(int estacionId) {
        this.estacionId = estacionId;
    }
}
