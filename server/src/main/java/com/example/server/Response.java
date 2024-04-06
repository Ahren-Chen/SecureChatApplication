package com.example.server;

import java.io.Serializable;

import javax.crypto.SecretKey;

public class Response implements Serializable {
    private static final long serialVersionUID = 42L;
    private final SecretKey key;

    public Response(SecretKey key) {
        this.key = key;
    }

    public SecretKey getKey() {
        return this.key;
    }
}
