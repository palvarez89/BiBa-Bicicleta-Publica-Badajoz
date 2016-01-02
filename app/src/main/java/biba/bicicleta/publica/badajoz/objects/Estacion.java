package biba.bicicleta.publica.badajoz.objects;

public class Estacion {


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
}