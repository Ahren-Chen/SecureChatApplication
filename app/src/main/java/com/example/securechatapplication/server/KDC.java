package com.example.securechatapplication.server;

import com.example.securechatapplication.interfaces.KDCInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.util.HashMap;

public class KDC implements KDCInterface {
    private static HashMap<String, Key> keysDB;

    public KDC() {
        keysDB = new HashMap<>();
    }
    @Override
    public boolean login() {
        return false;
    }

    public static void main(String[] args) {
        try {
            // Create server socket

            try (ServerSocket serverSocket = new ServerSocket(5000)) {
                System.out.println("KDC is waiting for requests to connect...");
                while (true) {
                    // Accept slave connection
                    Socket socket = serverSocket.accept();
                    System.out.println("Request Received: " + socket);

                    // Create new thread to handle slave
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
    private Socket socket;

    public RequestHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            // Open input and output streams
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Read input from slave
            String message = in.readLine();
            System.out.println("Message from slave: " + message);

            // Process input (e.g., perform some task)

            // Send response to slave
            out.println("Task completed");

            // Close streams and socket
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}