package com.example.server;

import java.io.Serializable;
import java.util.Date;

public class Request implements Serializable {
    private static final long serialVersionUID = 42L;
    private final RequestTypes type;
    private final String username;
    private final String key;
    private final long timeStampMili;

    //Feel free to add more logic and constructor types to request
    public Request(RequestTypes type, String username) {
        this.type = type;
        this.username = username;
        this.key = null;

        Date date = new Date();
        this.timeStampMili = date.getTime();
    }

    public RequestTypes getType() {
        return this.type;
    }

    public String getUsername() {
        return this.username;
    }

    public String getKey() {
        return this.key;
    }

    public long getTimeStampMili() {
        return timeStampMili;
    }
}
