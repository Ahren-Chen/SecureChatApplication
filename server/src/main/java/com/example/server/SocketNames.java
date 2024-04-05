package com.example.server;

public enum SocketNames {
    KDCSocket(5000),
    AccountManagementSocket(5001);

    private final int value;

    private SocketNames(int value){
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
