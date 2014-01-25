package biba.bicicleta.publica.badajoz.fragments;

import java.util.Vector;

import biba.bicicleta.publica.badajoz.Estacion;

public interface ActivityCommunicator{
    public void passDataToActivity(Vector<Estacion> estaciones);
}