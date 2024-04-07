import com.example.server.EncryptionAES.AESUtil;

public class Sender {
    public static void main(String[] args) {
        String message = "Hello, this is a secret message!";
        SecretKey key = AESUtil.generateKey();
        String encryptedMessage = AESUtil.encrypt(message, key);

        // Send encryptedMessage to the receiver
    }
}
