package com.fitlife.model;

import java.util.List;

public class ListarProgresosResponse {
    private boolean success;
    private String message;
    private List<Progreso> progresos;


// Getters y Setters para ListarProgresosResponse

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Progreso> getProgresos() {
        return progresos;
    }

    public void setProgresos(List<Progreso> progresos) {
        this.progresos = progresos;
    }

}
