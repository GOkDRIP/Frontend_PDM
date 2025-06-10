package com.fitlife.model;

public class AgregarProgresoRequest {
    public String fecha;
    public double peso;
    public int calorias;
    public String observaciones;

    public AgregarProgresoRequest(String fecha, double peso, int calorias, String observaciones) {
        this.fecha = fecha;
        this.peso = peso;
        this.calorias = calorias;
        this.observaciones = observaciones;
    }
}