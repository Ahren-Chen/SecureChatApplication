package com.example.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

public class Response implements Serializable {
    private static final long serialVersionUID = 42L;
    private final SecretKey key;
    private final SealedObject obj;
    private final String username;
    private final long timeStampMili;
    private final ArrayList<String> users;
    private final Exception exception;


    public Response(SecretKey key, String username) {
        this.key = key;
        this.obj = null;
        this.username = username;

        Date date = new Date();
        this.timeStampMili = date.getTime();

        this.users = null;
        this.exception = null;
    }
    public Response(SealedObject obj, String username) {
        this.obj = obj;
        this.key = null;
        this.username = username;

        Date date = new Date();
        this.timeStampMili = date.getTime();

        this.users = null;
        this.exception = null;
    }

    public Response(SealedObject obj, String username, Exception e) {
        this.obj = obj;
        this.key = null;
        this.username = username;

        Date date = new Date();
        this.timeStampMili = date.getTime();

        this.users = null;
        this.exception = e;
    }

    public Response(ArrayList<String> users, String username) {
        this.key = null;
        this.obj = null;

        Date date = new Date();
        this.timeStampMili = date.getTime();
        this.username = username;

        this.users = users;
        this.exception = null;

    }

    public SecretKey getKey() {
        return this.key;
    }
    public SealedObject getObj() { return this.obj; }
    public String getUsername() { return this.username; }

    public long getTimeStampMili() {
        return timeStampMili;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public Exception getException() {
        return exception;
    }
}
