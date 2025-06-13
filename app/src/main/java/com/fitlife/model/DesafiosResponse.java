package com.fitlife.model;

import java.util.List;

public class DesafiosResponse {
    private boolean success;
    private String message;
    private List<Desafio> data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<Desafio> getData() { return data; }
}
