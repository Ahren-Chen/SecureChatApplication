package com.example.securechatapplication.MAP;

import com.example.securechatapplication.interfaces.MAPInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MediatedAuthenticationProtocol extends MAPInterface {

    public static boolean authenticateLogin(String username, String password)  {

        //TO DO: Send message to KDC with username, KDC returns an encrypted session key that is encrypted
        //with the hash of the password
        /*try {
            Socket socket = new Socket("localhost", 5000);

            // Open input and output streams
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException exception) {
            System.out.println("Critical Error: " + exception);
        }*/

        return true;
    }
}
