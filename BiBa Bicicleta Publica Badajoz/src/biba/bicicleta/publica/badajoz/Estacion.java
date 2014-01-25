package biba.bicicleta.publica.badajoz;

public class Estacion {
	int numero;
	String nombre;
	String estado;
	int disponible;
	int capacidad;
	float lat;
	float lon;

	public Estacion(int n, String nom, String est, int disp, int cap,
			float latit, float longit) {
		numero = n;
		nombre = nom;
		estado = est;
		disponible = disp;
		capacidad = cap;
		lat = latit;
		lon = longit;
	}

	public int getNumero() {
		return numero;
	}

	public String getNombre() {
		return nombre;
	}

	public String getEstado() {
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
