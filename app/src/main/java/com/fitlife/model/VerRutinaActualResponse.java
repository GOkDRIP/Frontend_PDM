// VerRutinaActualResponse.java
package com.fitlife.model;

import java.util.List;

public class VerRutinaActualResponse {
    private boolean success;
    private String message;
    private List<Rutina> data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<Rutina> getData() { return data; }
}
