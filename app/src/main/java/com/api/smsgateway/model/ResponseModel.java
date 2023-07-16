package com.api.smsgateway.model;

import com.google.gson.annotations.SerializedName;

public class ResponseModel {
    @SerializedName("success")
    private boolean success;

    @SerializedName("error")
    private boolean error;

    @SerializedName("callbackUrl")
    private  String callbackUrl;

    @SerializedName("userName")
    private String userName;

    public ResponseModel(boolean success, boolean error, String callbackUrl, String userName) {
        this.success = success;
        this.error = error;
        this.callbackUrl = callbackUrl;
        this.userName = userName;
    }


    public boolean isSuccess() {
        return success;
    }

    public boolean isError() {
        return error;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

