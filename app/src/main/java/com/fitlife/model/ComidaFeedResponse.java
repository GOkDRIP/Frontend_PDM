// File: ComidaFeedResponse.java
package com.fitlife.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ComidaFeedResponse {
    @SerializedName("exito")
    private boolean success;

    @SerializedName("data")
    private List<ComidaFeedItem> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ComidaFeedItem> getData() {
        return data;
    }

    public void setData(List<ComidaFeedItem> data) {
        this.data = data;
    }
}
