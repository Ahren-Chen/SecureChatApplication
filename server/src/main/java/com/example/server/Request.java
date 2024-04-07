package com.example.server;

import java.io.Serializable;
import java.util.Date;

import javax.crypto.SealedObject;

public class Request implements Serializable {
    private static final long serialVersionUID = 42L;
    private final RequestTypes type;
    private final String username;
    private final long timeStampMili;
    private final SealedObject obj;

    //Feel free to add more logic and constructor types to request
    public Request(RequestTypes type, String username) {
        this.type = type;
        this.username = username;

        Date date = new Date();
        this.timeStampMili = date.getTime();

        this.obj = null;
    }

    public Request(SealedObject obj, String username) {
        this.type = null;
        this.username = username;

        Date date = new Date();
        this.timeStampMili = date.getTime();

        this.obj = obj;
    }

    public RequestTypes getType() {
        return this.type;
    }

    public String getUsername() {
        return this.username;
    }

    public long getTimeStampMili() {
        return timeStampMili;
    }

    public SealedObject getObj() {
        return obj;
    }
}
