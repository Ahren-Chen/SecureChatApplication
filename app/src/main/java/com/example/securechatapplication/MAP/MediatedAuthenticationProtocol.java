package com.example.securechatapplication.MAP;

import com.example.securechatapplication.interfaces.MAPInterface;

public class MediatedAuthenticationProtocol extends MAPInterface {

    public static boolean authenticateLogin(String username, String password) {

        //TO DO: Send message to KDC with username, KDC returns an encrypted session key that is encrypted
        //with the hash of the password
        return true;
    }
}
