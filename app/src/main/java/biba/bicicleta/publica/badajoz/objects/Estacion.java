package biba.bicicleta.publica.badajoz.objects;

public class Estacion {
    int numero;
    String nombre;
    boolean estado;
    int disponible;
    int capacidad;
    float lat;
    float lon;

    public Estacion(int n, String nom, String est, int disp, int cap,
                    float latit, float longit) {
        numero = n;
        nombre = nom;
        disponible = disp;
        capacidad = cap;
        lat = latit;
        lon = longit;
        estado = true;
        if (est.indexOf("FUERA") != -1) {
            estado = false;
        }
    }

    public int getNumero() {
        return numero;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean getEstado() {
        return estado;
    }

    public int getDisponibles() {
        return disponible;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public int getEspacio() {
        return capacidad - disponible;
    }

    public float getLatitude() {
        return lat;
    }

    public float getLongitude() {
        return lon;
    }

}