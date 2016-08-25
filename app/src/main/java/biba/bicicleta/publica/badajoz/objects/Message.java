package biba.bicicleta.publica.badajoz.objects;

import java.sql.Timestamp;

public class Message {
    private String message;
    private Timestamp date;

    public Message(String msg, Timestamp time) {
        message = msg;
        date = time;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getDate() {
        return date;
    }
}
