package com.example.server;

import static com.example.server.SocketNames.AccountManagementSocket;
import static com.example.server.SocketNames.KDCSocket;

import com.example.server.EncryptionAES.AESUtil;
import com.example.server.interfaces.KDCInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.util.HashMap;
import java.util.Objects;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class KDC implements KDCInterface {
    private static HashMap<String, HashMap<SecretKey, Long>> keysDB;

    public static void main(String[] args) {
        keysDB = new HashMap<>();
        keysDB.put("ahren657", new HashMap<>());
        keysDB.put("John", new HashMap<>());
        try {
            // Create server socket

            try (ServerSocket serverSocket = new ServerSocket(KDCSocket.getValue())) {
                System.out.println("KDC is waiting for requests to connect...");
                //serverSocket.getInetAddress();
                System.out.println("Server IP Address: " + serverSocket.getInetAddress().getHostAddress());
                while (true) {
                    // Accept request connection
                    Socket socket = serverSocket.accept();
                    System.out.println("Request Received: " + socket);

                    // Create new thread to handle request
                    KDCRequestHandler requestHandler = new KDCRequestHandler(socket);
                    Thread t = new Thread(requestHandler);
                    t.start();

                    try {
                        t.join();
                    } catch(InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    //Assuming that I am logging in, get new keys and timestamps and store them
                    SecretKey newKey = requestHandler.getNewKey();
                    Long timeStamp = requestHandler.getNewKeyTimeStamp();
                    String username = requestHandler.getUsername();
                    if (newKey != null && username != null && requestHandler.isSuccess()) {
                        HashMap<SecretKey, Long> allUserKeys = keysDB.get(username);
                        allUserKeys.put(newKey, timeStamp);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Slave handler class
class KDCRequestHandler implements Runnable {
    private final Socket socket;
    private SecretKey newKey;
    private Long newKeyTimeStamp;
    private String username;
    private boolean success = false;
    public KDCRequestHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            // Open input and output streams
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Read input from slave
            Request message = (Request) in.readObject();
            System.out.println("Request from MAP: " + message.getType().toString());

            // Process input (e.g., perform some task)
            if (message.getType() == RequestTypes.login) {
                SecretKey sessionKey = AESUtil.generateKey();
                System.out.println("KDC sessionKey: " + sessionKey.toString());
                Response KDCResponse;

                //Connect to the Account Management server
                try (Socket accountManagementSocket = new Socket(InetAddress.getLocalHost().getHostAddress(), AccountManagementSocket.getValue())) {
                    ObjectOutputStream accountOut = new ObjectOutputStream(accountManagementSocket.getOutputStream());
                    ObjectInputStream accountIn = new ObjectInputStream(accountManagementSocket.getInputStream());

                    //Request the stored secretKey made from password for user
                    Request getAccountSecretKey = new Request(RequestTypes.getUserSecretKey, message.getUsername());
                    accountOut.writeObject(getAccountSecretKey);
                    System.out.println("Sent request to Account Management");

                    //Get response
                    Response accountResponse = null;
                    while (accountResponse == null) {
                        accountResponse = (Response) accountIn.readObject();
                    }

                    if (Objects.equals(accountResponse.getUsername(), "User not found")) {
                        KDCResponse = new Response((SecretKey) null, message.getUsername());
                        success = false;
                    }
                    else {
                        System.out.println("Received response from Account Management");
                        SecretKey accountSecretKey = accountResponse.getKey();

                        //Encrypt the session key with password secret key
                        SealedObject encryptedKDCKey = AESUtil.encryptObject(sessionKey, accountSecretKey);
                        KDCResponse = new Response(encryptedKDCKey, message.getUsername());
                        success = true;
                    }
                    accountIn.close();
                    accountOut.close();
                }

                //Record some information and send the package back to MAP
                this.newKey = sessionKey;
                this.newKeyTimeStamp = KDCResponse.getTimeStampMili();
                this.username = message.getUsername();
                out.writeObject(KDCResponse);
                System.out.println("Sent response to MAP");
            }

            // Close streams and socket
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public long getNewKeyTimeStamp() {
        return newKeyTimeStamp;
    }

    public SecretKey getNewKey() {
        return newKey;
    }

    public String getUsername() {
        return username;
    }

    public boolean isSuccess() {
        return success;
    }
}