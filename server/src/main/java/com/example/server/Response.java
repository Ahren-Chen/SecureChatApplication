package com.example.server;

import java.io.Serializable;
import java.util.Date;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

public class Response implements Serializable {
    private static final long serialVersionUID = 42L;
    private final SecretKey key;
    private final SealedObject obj;
    private final String username;
    private final long timeStampMili;

    public Response(SecretKey key, String username) {
        this.key = key;
        this.obj = null;
        this.username = username;

        Date date = new Date();
        this.timeStampMili = date.getTime();
    }
    public Response(SealedObject obj, String username) {
        this.obj = obj;
        this.key = null;
        this.username = username;

        Date date = new Date();
        this.timeStampMili = date.getTime();
    }

    public SecretKey getKey() {
        return this.key;
    }
    public SealedObject getObj() { return this.obj; }
    public String getUsername() { return this.username; }

    public long getTimeStampMili() {
        return timeStampMili;
    }
}
