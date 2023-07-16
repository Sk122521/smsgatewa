package com.api.smsgateway.model;

import com.google.gson.annotations.SerializedName;

public class ChecksentRequest {

    @SerializedName("id")
    private String id;

    @SerializedName("status")
    private String status;


    public ChecksentRequest(String id, String status) {
        this.id = id;
        this.status = status;
    }
}
