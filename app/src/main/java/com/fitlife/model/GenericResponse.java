// app/src/main/java/com/fitlife/model/GenericResponse.java
package com.fitlife.model;

import com.google.gson.annotations.SerializedName;

public class GenericResponse {
    @SerializedName("exito")
    public boolean success;

    @SerializedName("mensaje")
    public String message;
}
