import com.example.server.EncryptionAES.AESUtil;

public class Receiver {
    public static void main(String[] args) {
        // Receive encryptedMessage from the sender

        // Assume key is already received or negotiated with the sender
        SecretKey key = getSharedKeyFromSender();

        // Decrypt the received encryptedMessage
        String decryptedMessage = AESUtil.decrypt(encryptedMessage, key);
        System.out.println("Decrypted Message: " + decryptedMessage);
    }

    private static SecretKey getSharedKeyFromSender() {
        // Implement key exchange mechanism (e.g., using Key Distribution Centre)
        // or pre-shared key
        // For simplicity, returning a randomly generated key
        return AESUtil.generateKey();
    }
}
