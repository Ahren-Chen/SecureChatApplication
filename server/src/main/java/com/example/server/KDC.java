package com.example.server;

import static com.example.server.SocketNames.KDCSocket;

import com.example.server.interfaces.KDCInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

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
                    Thread t = new Thread(new RequestHandler(socket));
                    t.start();

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Slave handler class
class RequestHandler implements Runnable {
    private final Socket socket;

    public RequestHandler(Socket socket) {
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
            out.writeObject(generateKey());

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

    public static SecretKey generateKey() {
        SecretKey key;
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(256);
            key = generator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return key;
    }
}