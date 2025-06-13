package com.fitlife.model;

import java.util.List;

public class RecuperarContrasenaResponse {
    private boolean success;
    private String message;
    private List<?> data;

    public RecuperarContrasenaResponse() {}

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<?> getData() {
        return data;
    }
}
