package com.fitlife.model;

public class RegistrarComidaRequest {
    public String nombre;
    public int calorias;
    public double carbohidratos;
    public double proteinas;
    public double grasas;
    public String observaciones;

    // Constructor opcional
    public RegistrarComidaRequest(String nombre,
                                  int calorias,
                                  double carbohidratos,
                                  double proteinas,
                                  double grasas,
                                  String observaciones) {
        this.nombre = nombre;
        this.calorias = calorias;
        this.carbohidratos = carbohidratos;
        this.proteinas = proteinas;
        this.grasas = grasas;
        this.observaciones = observaciones;
    }
}
