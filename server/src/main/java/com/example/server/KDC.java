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
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class KDC implements KDCInterface {
    private static HashMap<String, Key> keysDB;

    public KDC() {
        keysDB = new HashMap<>();
    }

    public static void main(String[] args) {
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
                    Thread t = new Thread(new KDCRequestHandler(socket));
                    t.start();

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

    public KDCRequestHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            // Open input and output streams
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            // Read input from slave
            Request message = (Request) in.readObject();
            System.out.println("Request from MAP: " + message.getType().toString());

            // Process input (e.g., perform some task)
            if (message.getType() == RequestTypes.login) {
                SecretKey sessionKey = AESUtil.generateKey();
                IvParameterSpec iv = AESUtil.generateIv();
                Response KDCResponse;

                try (Socket accountManagementSocket = new Socket(InetAddress.getLocalHost().getHostAddress(), AccountManagementSocket.getValue())) {
                    ObjectInputStream accountIn = new ObjectInputStream(accountManagementSocket.getInputStream());
                    ObjectOutputStream accountOut = new ObjectOutputStream(accountManagementSocket.getOutputStream());

                    Request getAccountSecretKey = new Request(RequestTypes.getUserSecretKey, message.getUsername());
                    accountOut.writeObject(getAccountSecretKey);

                    Response accountResponse = null;
                    while (accountResponse == null) {
                        accountResponse = (Response) accountIn.readObject();
                    }

                    SecretKey accountSecretKey = accountResponse.getKey();

                    SealedObject encryptedKDCKey = AESUtil.encryptObject(sessionKey, accountSecretKey, iv);
                    KDCResponse = new Response(encryptedKDCKey, iv, message.getUsername());
                    accountIn.close();
                    accountOut.close();
                }

                out.writeObject(KDCResponse);
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
}