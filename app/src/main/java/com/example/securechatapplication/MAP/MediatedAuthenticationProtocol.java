package com.example.securechatapplication.MAP;

import com.example.server.EncryptionAES.AESUtil;
import com.example.server.Response;
import com.example.server.interfaces.MAPInterface;
import com.example.server.Request;
import com.example.server.RequestTypes;
import com.example.server.SocketNames;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class MediatedAuthenticationProtocol extends MAPInterface {

    public static boolean authenticateLogin(String username, String password)  {

        Request request = new Request(RequestTypes.login, username);

        Thread t = new Thread(new NetworkHandler(request, username, password));
        t.start();
        //TO DO: Send message to KDC with username, KDC returns an encrypted session key that is encrypted
        //with the hash of the password
        try {
            t.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted execution: " + e);
        }
        return true;
    }
}

class NetworkHandler implements Runnable {
    private final Request request;
    private final String password;
    private final String username;
    public NetworkHandler(Request request) {
        this.request = request;
        this.password = null;
        this.username = null;
    }
    public NetworkHandler(Request request, String username, String password) {
        this.request = request;
        this.password = password;
        this.username = username;
    }

    @Override
    public void run() {
        String emulatorHostAddress = "10.0.2.2";
        try (Socket socket = new Socket(emulatorHostAddress, SocketNames.KDCSocket.getValue())){

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(request);
            Response response = null;
            while (response == null) {
                response = (Response) in.readObject();
            }

            SealedObject encryptedSessionKey = response.getObj();
            IvParameterSpec iv = response.getIv();
            String username = response.getUsername();

            if (encryptedSessionKey == null || iv == null) {
                throw new RuntimeException("Response contains nulls from KDC");
            }
            else if (!Objects.equals(username, this.username)) {
                throw new RuntimeException("Response to incorrect user");
            }

            SecretKey passwordSecretKey = AESUtil.getKeyFromPassword(this.password, this.password);
            SecretKey sessionKey = (SecretKey) AESUtil.decryptObject(encryptedSessionKey, passwordSecretKey, iv);

            System.out.println(sessionKey.toString());

            out.close();
            in.close();

        } catch (IOException exception) {
            System.out.println("Critical Error: " + exception);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}