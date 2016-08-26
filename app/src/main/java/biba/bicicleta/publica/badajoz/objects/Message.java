package biba.bicicleta.publica.badajoz.objects;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;

public class Message {
    private String message;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Date date;

    public Message(String msg, Date time) {
        message = msg;
        date = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
