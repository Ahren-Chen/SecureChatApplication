package com.example.client;

import com.example.server.EncryptionAES.AESUtil;

import javax.crypto.SecretKey;

public class Sender {
    public static void sendMessage(String message, SecretKey key) {
        String encryptedMessage = AESUtil.encrypt(message, key);

        // sending encryptedMessage to the receiver
        System.out.println("Sending encrypted message: " + encryptedMessage);
    }
}
