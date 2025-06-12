// ObjetivosDiaResponse.java
package com.fitlife.model;

import java.util.List;

public class ObjetivosDiaResponse {
    private boolean success;
    private String message;
    private List<ObjetivoDiaResponse> data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<ObjetivoDiaResponse> getData() { return data; }
}
