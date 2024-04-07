package com.example.server;

public class TimeOutException extends Exception {
    public TimeOutException() {
        super("Login session has expired");
    }
}
