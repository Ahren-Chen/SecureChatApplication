package com.example.server.EncryptionAES;

import javax.crypto.SecretKey;

public class Receiver {
    public static void main(String[] args) {
        SecretKey key = getSharedKeyFromSender();

        // Simulate continuous receiving of encrypted messages
        while (true) {
            // Receive encryptedMessage from the sender
            // In a real application, this part would involve network communication
            String encryptedMessage = receiveEncryptedMessage();

            // Decrypt the received encryptedMessage
            String decryptedMessage = AESUtil.decrypt(encryptedMessage, key);
            System.out.println("Received decrypted message: " + decryptedMessage);
        }
    }

    private static SecretKey getSharedKeyFromSender() {
        // Implement key exchange mechanism
        // For simplicity, returning a randomly generated key
        return AESUtil.generateKey();
    }

    private static String receiveEncryptedMessage() {
        // Simulate receiving encrypted message from sender
        // In a real application, this part would involve network communication
        return "Encrypted message from sender";
    }
}
