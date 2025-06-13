package com.fitlife.model;

import java.util.List;

public class RestablecerContrasenaResponse {
    private boolean success;
    private String message;
    private List<?> data;

    public RestablecerContrasenaResponse() {}

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
