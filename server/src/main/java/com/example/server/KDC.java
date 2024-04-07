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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class KDC implements KDCInterface {
    private static HashMap<String, SecretKey> sessionKeysDB;
    private static HashMap<List<String>, SecretKey> chatKeysDB;
    private static HashMap<SecretKey, Long> timeStampDB;

    public static void main(String[] args) {
        sessionKeysDB = new HashMap<>();
        chatKeysDB = new HashMap<>();
        timeStampDB = new HashMap<>();
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
                    KDCRequestHandler requestHandler = new KDCRequestHandler(socket, sessionKeysDB, timeStampDB, chatKeysDB);
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
                        sessionKeysDB.put(username, newKey);
                        timeStampDB.put(newKey, timeStamp);
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
    private final HashMap<String, SecretKey> sessionKeyDB;
    private final HashMap<SecretKey, Long> timeStampDB;
    private final HashMap<List<String>, SecretKey> chatKeysDB;
    public KDCRequestHandler(Socket socket, HashMap<String, SecretKey> sessionKeysDB, HashMap<SecretKey, Long> timeStampDB,
                             HashMap<List<String>, SecretKey> chatKeysDB) {
        this.socket = socket;
        this.sessionKeyDB = sessionKeysDB;
        this.timeStampDB = timeStampDB;
        this.chatKeysDB = chatKeysDB;
    }

    public void run() {
        try {
            // Open input and output streams
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Read input from MAP
            Request message = (Request) in.readObject();
            System.out.println("Request from MAP: " + message.getType());

            // Process input (e.g., perform some task)
            if (message.getType() == RequestTypes.login) {
                SecretKey sessionKey = AESUtil.generateKey();
                username = message.getUsername();
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
                        System.out.println("User not found");
                        success = false;
                    }
                    else {
                        System.out.println("Received response from Account Management");
                        SecretKey accountSecretKey = accountResponse.getKey();

                        //Encrypt the session key with password secret key
                        Response username_Key_package = new Response(sessionKey, username);
                        this.newKeyTimeStamp = username_Key_package.getTimeStampMili();

                        SealedObject encryptedKDCKey = AESUtil.encryptObject(username_Key_package, accountSecretKey);
                        KDCResponse = new Response(encryptedKDCKey, message.getUsername());
                        success = true;
                    }
                    accountIn.close();
                    accountOut.close();
                }

                //Record some information and send the package back to MAP
                this.newKey = sessionKey;
                this.username = message.getUsername();
                out.writeObject(KDCResponse);
                System.out.println("Sent response to MAP, logging in: " + this.success);
            }

            else if (message.getType() == null) {
                SecretKey sessionKey = sessionKeyDB.get(message.getUsername());
                Date date = new Date();
                Long keyTimeStamp = timeStampDB.get(sessionKey);
                Response KDCResponse;

                if (keyTimeStamp == null) {
                    throw new RuntimeException("No key found in time stamp DB");
                }

                if (date.getTime() - keyTimeStamp < 7.2 * Math.pow(10, 6)) {
                    SealedObject encryptedType = message.getObj();

                    RequestTypes unencryptedType;
                    try {
                        unencryptedType = (RequestTypes) AESUtil.decryptObject(encryptedType, sessionKey);
                    } catch (BadPaddingException e) {
                        throw new RuntimeException(e);
                    }

                    if (unencryptedType == RequestTypes.getAllUsers) {
                        try (Socket accountManagementSocket = new Socket(InetAddress.getLocalHost().getHostAddress(), AccountManagementSocket.getValue())) {
                            ObjectOutputStream accountOut = new ObjectOutputStream(accountManagementSocket.getOutputStream());
                            ObjectInputStream accountIn = new ObjectInputStream(accountManagementSocket.getInputStream());

                            //Request the stored secretKey made from password for user
                            Request getAccountSecretKey = new Request(RequestTypes.getAllUsers, message.getUsername());
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
                            } else {
                                System.out.println("Received response from Account Management");
                                ArrayList<String> accountUsers = accountResponse.getUsers();
                                SealedObject encryptedUsers = AESUtil.encryptObject(accountUsers, sessionKey);

                                KDCResponse = new Response(encryptedUsers, message.getUsername());
                                success = true;
                            }
                            accountIn.close();
                            accountOut.close();
                        }
                    }
                    else {
                        KDCResponse = new Response((SecretKey) null, message.getUsername());
                        success = false;
                    }
                }
                else {
                    success = false;
                    KDCResponse = new Response((SecretKey) null, message.getUsername());
                }

                out.writeObject(KDCResponse);
                System.out.println("Sent response to MAP, get users: " + this.success);
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

    public Long getNewKeyTimeStamp() {
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