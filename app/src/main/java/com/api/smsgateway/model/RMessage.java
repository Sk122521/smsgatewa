package com.api.smsgateway.model;

import android.graphics.Bitmap;

public class RMessage {
    private String address;
    private String body;
    private long date;
    private int type;

    private String contactname;


    public RMessage(String address, String body, long date,int type,String contactname) {
        this.address = address;
        this.body = body;
        this.date = date;
        this.type =type;
        this.contactname = contactname;
       // this.contactphoto = contactphoto;
    }

    public String getAddress() {
        return address;
    }

    public String getBody() {
        return body;
    }

    public long getDate() {
        return date;
    }

    public int getType() {
        return type;
    }

    public String getContactname() {
        return contactname;
    }
}

