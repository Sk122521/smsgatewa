package com.api.smsgateway.model;

import com.google.gson.annotations.SerializedName;

public class RequestModel {
    @SerializedName("login")
    private String login;

    @SerializedName("password")
    private String password;

    @SerializedName("deviceId")
    private String deviceId;

    @SerializedName("deviceName")
    private String deviceName;



    public RequestModel(String login, String password, String deviceId, String deviceName) {
        this.login = login;
        this.password = password;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
    }

    // Getters and setters
}

