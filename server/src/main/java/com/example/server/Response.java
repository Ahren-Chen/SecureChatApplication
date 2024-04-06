package com.example.server;

import java.io.Serializable;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Response implements Serializable {
    private static final long serialVersionUID = 42L;
    private final SecretKey key;
    private final SealedObject obj;
    private final IvParameterSpec iv;
    private final String username;

    public Response(SecretKey key, String username) {
        this.key = key;
        this.iv = null;
        this.obj = null;
        this.username = username;
    }
    public Response(SealedObject obj, IvParameterSpec iv, String username) {
        this.obj = obj;
        this.iv = iv;
        this.key = null;
        this.username = username;
    }

    public SecretKey getKey() {
        return this.key;
    }
    public SealedObject getObj() { return this.obj; }
    public IvParameterSpec getIv() { return this.iv; }
    public String getUsername() { return this.username; }
}
