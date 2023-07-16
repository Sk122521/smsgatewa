package com.api.smsgateway.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageResponse {

    @SerializedName("success")
    private String success;

    @SerializedName("error")
    private String error;
    @SerializedName("messages")
    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }

    public String getSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }
}
