package com.api.smsgateway.model;

import com.google.gson.annotations.SerializedName;

public class ReceiveRequest {

    @SerializedName("senderNumber")
    private String senderNumber;

    @SerializedName("receiverNumber")
    private String receiverNumber;

    @SerializedName("smsContent")
    private String smsContent;

    @SerializedName("deviceId")
    private String deviceId;

    public ReceiveRequest(String senderNumber, String receiverNumber, String smsContent, String deviceId) {
        this.senderNumber = senderNumber;
        this.receiverNumber = receiverNumber;
        this.smsContent = smsContent;
        this.deviceId = deviceId;
    }
}
