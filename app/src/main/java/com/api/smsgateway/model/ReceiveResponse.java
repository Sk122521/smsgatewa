package com.api.smsgateway.model;

import com.google.gson.annotations.SerializedName;

public class ReceiveResponse {

    @SerializedName("callbackUrl")
    private String callbackUrl;

    public String getCallbackUrl() {
        return callbackUrl;
    }
}
