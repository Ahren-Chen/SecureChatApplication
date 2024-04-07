package com.example.server;

import static com.example.server.SocketNames.AccountManagementSocket;

import com.example.server.EncryptionAES.AESUtil;
import com.example.server.interfaces.AccountManagementInterface;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AccountManagement implements AccountManagementInterface {
    private static List<HashMap<String, String>> accounts;

    @Override
    public List<HashMap<String, String>> getAccounts() {
        return accounts;
    }

    public boolean createAccount(String newName, String newPass, String auth) {
        for (HashMap<String, String> h : accounts) {
            if (h.containsValue(newName)) {
                System.out.println("Error: user name already in system");
                return false;
            }
        }

        SecretKey userSecretKey = AESUtil.getKeyFromPassword(newPass, newPass);
        byte[] userSecretKeyData = userSecretKey.getEncoded();

        HashMap<String, String> newUser = new HashMap<>();
        newUser.put("username", newName);
        newUser.put("secretPasswordKey", Base64.getEncoder().encodeToString(userSecretKeyData));
        newUser.put("authorityLevel", auth);
        accounts.add(newUser);
        return true;
    }

    public boolean deleteAccount(String name) {
        for (HashMap<String, String> h : accounts) {
            if (h.containsValue(name)) {
                if (!accounts.remove(h))
                    System.out.println("Error deleting account");
                return true;
            }
        }
        System.out.println("Could not find account to delete");
        return false;
    }

    public boolean editAccount(String oldName, String newName, String newPass, String newAuth) {

        for (HashMap<String, String> h : accounts) {
            if (h.containsValue(newName) && !(newName.equals(oldName))) {
                System.out.println("Name is taken by another user!");
                return false;
            }
        }

        for (HashMap<String, String> h : accounts) {

            SecretKey userSecretKey = AESUtil.getKeyFromPassword(newPass, newPass);
            byte[] userSecretKeyData = userSecretKey.getEncoded();

            if (h.containsValue(oldName)) {
                h.replace("username", newName);
                h.replace("secretPasswordKey", Base64.getEncoder().encodeToString(userSecretKeyData));
                h.replace("authorityLevel", newAuth);
                return true;
            }


        }

        System.out.println("Error updating user info");
        return false;
    }

    public void start() {
        accounts = new ArrayList<>();

        HashMap<String, String> user1 = new HashMap<>();
        String user1Password = "abcd";
        SecretKey user1SecretKey = AESUtil.getKeyFromPassword(user1Password, user1Password);
        byte[] user1SecretKeyData = user1SecretKey.getEncoded();

        user1.put("username", "ahren657");
        user1.put("secretPasswordKey", Base64.getEncoder().encodeToString(user1SecretKeyData));
        user1.put("authorityLevel", "CEO");

        HashMap<String, String> user2 = new HashMap<>();
        String user2Password = "1234";
        SecretKey user2SecretKey = AESUtil.getKeyFromPassword(user2Password, user2Password);
        byte[] user2SecretKeyData = user2SecretKey.getEncoded();

        user2.put("username", "John");
        user2.put("secretPasswordKey", Base64.getEncoder().encodeToString(user2SecretKeyData));
        user2.put("authorityLevel", "Employee");

        accounts.add(user1);
        accounts.add(user2);

        try (ServerSocket serverSocket = new ServerSocket(AccountManagementSocket.getValue())) {
            System.out.println("Account Management Server is waiting for requests to connect...");
            while (true) {
                // Accept request connection
                Socket socket = serverSocket.accept();
                System.out.println("Request Received: " + socket);

                // Open input and output streams
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                System.out.println("Streams opened");

                try {
                    Request message = null;
                    System.out.println("Waiting for request...");
                    while (message == null) {
                        message = (Request) in.readObject();
                    }

                    if (message.getType() == RequestTypes.getUserSecretKey) {
                        String username = message.getUsername();
                        System.out.println("Received request for secret key from " + username);
                        if (username != null) {
                            boolean found = false;

                            for (HashMap<String, String> account : accounts) {
                                if (Objects.equals(account.get("username"), username)) {
                                    found = true;
                                    String encodedKey = account.get("secretPasswordKey");
                                    byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
                                    out.writeObject(new Response(
                                            new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"), username));
                                    System.out.println("Sent secret key to KDC");
                                    break;
                                }
                            }

                            if (!found) {
                                System.out.println("User not found");
                                out.writeObject(new Response((SecretKey)null, "User not found"));
                            }
                        }
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                // Close streams and socket
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
