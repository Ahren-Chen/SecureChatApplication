package com.example.securechatapplication.MAP;

import com.example.server.interfaces.MAPInterface;
import com.example.server.Request;
import com.example.server.RequestTypes;
import com.example.server.SocketNames;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

import javax.crypto.SecretKey;

public class MediatedAuthenticationProtocol extends MAPInterface {

    public static boolean authenticateLogin(String username, String password)  {

        Request request = new Request(RequestTypes.login, username);

        Thread t = new Thread(new NetworkHandler(request));
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
    public NetworkHandler(Request request) {
        this.request = request;
    }

    @Override
    public void run() {
        String emulatorHostAddress = "10.0.2.2";
        try (Socket socket = new Socket(emulatorHostAddress, SocketNames.KDCSocket.getValue())){

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(request);
            SecretKey key = null;
            while (key == null) {
                key = (SecretKey) in.readObject();
            }

            System.out.println(Arrays.toString(key.getEncoded()));

            out.close();
            in.close();

        } catch (IOException exception) {
            System.out.println("Critical Error: " + exception);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}