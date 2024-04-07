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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

public class MediatedAuthenticationProtocol extends MAPInterface {
    private static HashMap<String, SecretKey> userKeys = new HashMap<>();
    public static boolean authenticateLogin(String username, String password)  {

        //Creating new request type login
        Request request = new Request(RequestTypes.login, username);

        //Sending the request and handling the feedback in a thread because Android doesn't want me to do
        //this on the main thread.
        NetworkHandler networkHandler = new NetworkHandler(request, username, password);
        Thread t = new Thread(networkHandler);
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted execution: " + e);
        }

        if (networkHandler.result()) {
            userKeys.put("sessionKey", networkHandler.getSessionKey());
            return true;
        }
        else {
            return false;
        }
    }

    public static ArrayList<HashMap<String, String>> getAllUsers(String username) {
        Request request = new Request(RequestTypes.getAllUsers, username);

        SecretKey sessionKey = userKeys.get("sessionKey");
        if (sessionKey == null) {
            throw new RuntimeException("No session key");
        }

        //Sending the request and handling the feedback in a thread because Android doesn't want me to do
        //this on the main thread.
        NetworkHandler networkHandler = new NetworkHandler(request);
        Thread t = new Thread(networkHandler);
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted execution: " + e);
        }

        if (networkHandler.result()) {
            return networkHandler.getUsers();
        }
        else {
            return null;
        }
    }
}

class NetworkHandler implements Runnable {
    private final Request request;
    private final String password;
    private final String username;
    private ArrayList<HashMap<String, String>> users;
    private boolean success = false;
    private SecretKey sessionKey = null;
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
        //THIS IS WHERE KERBEROS IS USED FOR LOGIN

        //Setting up a socket to connect to the already running KDC server
        String emulatorHostAddress = "10.0.2.2";
        SealedObject encryptedPackage;
        String username;

        try (Socket socket = new Socket(emulatorHostAddress, SocketNames.KDCSocket.getValue())){

            //Getting the input and output streams
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            //Send the request over
            out.writeObject(request);

            //Wait for the response
            Response response = null;
            while (response == null) {
                response = (Response) in.readObject();
            }
            System.out.println("Received Response from KDC");

            //Close channels once response is back
            out.close();
            in.close();

            //This is purely for login now, but grouped together as general
            //Get the encrypted sessionKey from logging in
            encryptedPackage = response.getObj();
            username = response.getUsername();

        } catch (IOException exception) {
            throw new RuntimeException("Critical Error: " + exception);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (!Objects.equals(username, this.username)) {
            this.success = false;
            System.out.println("Sent to wrong user");
        }
        else if (encryptedPackage == null) {
            System.out.println("No key received");
            this.success = false;
        }
        else if (request.getType() == RequestTypes.login){
            //Decrypt the key and store it
            SecretKey passwordSecretKey = AESUtil.getKeyFromPassword(this.password, this.password);
            try {
                Response unencryptedPackage = (Response) AESUtil.decryptObject(encryptedPackage, passwordSecretKey);
                this.sessionKey = unencryptedPackage.getKey();

                this.success = Objects.equals(unencryptedPackage.getUsername(), this.username);
            } catch (BadPaddingException e) {
                this.success = false;
            }
        }
        else if (request.getType() == RequestTypes.getAllUsers) {
            try {

                @SuppressWarnings("unchecked")
                ArrayList<HashMap<String, String>> unencryptedPackage = (ArrayList<HashMap<String, String>>)
                        AESUtil.decryptObject(encryptedPackage, sessionKey);

                this.users = unencryptedPackage;
                this.success = true;

            } catch (BadPaddingException e) {
                throw new RuntimeException("Session key is wrong for decryption");
            }
        }
    }

    public SecretKey getSessionKey() {
        return this.sessionKey;
    }

    public boolean result() {
        return this.success;
    }

    public ArrayList<HashMap<String, String>> getUsers() {
        return users;
    }
}