package com.fitlife.model;

public class RecuperarContrasenaRequest {
    private String email;

    public RecuperarContrasenaRequest() {}

    public RecuperarContrasenaRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
