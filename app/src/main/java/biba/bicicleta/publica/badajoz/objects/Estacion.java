package biba.bicicleta.publica.badajoz.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Estacion  implements Parcelable {


    private int n;
    private String name;
    private float lat;
    private float lon;
    private String state;
    private int avail;
    private int total;


    public Estacion() {
    }

    public int getN() {
        return this.n;
    }

    public void setN(int value) {
        this.n = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public float getLat() {
        return this.lat;
    }

    public void setLat(float value) {
        this.lat = value;
    }

    public float getLon() {
        return this.lon;
    }

    public void setLon(float value) {
        this.lon = value;
    }

    public String getState() {
        return this.state;
    }

    public boolean getStateBool() {
        return !this.state.contains("FUERA");
    }


    public void setState(String value) {
        this.state = value;
    }

    public int getAvail() {
        return this.avail;
    }

    public void setAvail(int value) {
        this.avail = value;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int value) {
        this.total = value;
    }

    public int getSpace() {
        return this.total - this.avail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(n);
        parcel.writeString(name);
        parcel.writeFloat(lat);
        parcel.writeFloat(lon);
        parcel.writeString(state);
        parcel.writeInt(avail);
        parcel.writeInt(total);
    }

    public static final Parcelable.Creator<Estacion> CREATOR = new Parcelable.Creator<Estacion>() {
        public Estacion createFromParcel(Parcel in) {
            return new Estacion(in);
        }

        public Estacion[] newArray(int size) {
            return new Estacion[size];
        }
    };

    private Estacion(Parcel in) {
        n = in.readInt();
        name = in.readString();
        lat = in.readFloat();
        lon = in.readFloat();
        state = in.readString();
        avail = in.readInt();
        total = in.readInt();
    }
}