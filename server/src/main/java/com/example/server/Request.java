package com.example.server;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 42L;
    private final RequestTypes type;
    private final String username;
    private final String key;

    //Feel free to add more logic and constructor types to request
    public Request(RequestTypes type, String username) {
        this.type = type;
        this.username = username;
        this.key = null;
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

}
