package biba.bicicleta.publica.badajoz.fragments;

import java.util.Vector;

import biba.bicicleta.publica.badajoz.Estacion;

public interface FragmentCommunicator {
	public void passDataToFragment(Vector<Estacion> estaciones);
}