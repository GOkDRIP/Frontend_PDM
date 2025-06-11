package com.fitlife.model;

import java.util.List;

public class ListarComidasResponse {
    public boolean success;
    public String message;
    public List<ComidaResponse> comidas;
    public int totalCalorias;
    public int caloriasRec;
}
