package com.fitlife.model;

import com.google.gson.annotations.SerializedName;

public class AnalisisResponse {
    @SerializedName("exito")
    public boolean success;

    @SerializedName("mensaje")
    public String message;

    @SerializedName("etiqueta")
    public String etiqueta;

    @SerializedName("calorias")
    public double calorias;

    @SerializedName("proteinas")
    public double proteinas;

    @SerializedName("grasas")
    public double grasas;

    @SerializedName("carbohidratos")
    public double carbohidratos;
}
