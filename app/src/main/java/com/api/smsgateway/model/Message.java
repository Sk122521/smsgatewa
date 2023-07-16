package com.api.smsgateway.model;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("id")
    private String id;

    @SerializedName("message")
    private String message;

    @SerializedName("number")
    private String recipient;

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getRecipient() {
        return recipient;
    }
}
