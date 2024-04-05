package com.example.server;

import com.example.server.interfaces.AccountManagementInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AccountManagement implements AccountManagementInterface {
    private static List<HashMap<String, String>> accounts;

    public AccountManagement() {
        accounts = new ArrayList<>();

        HashMap<String, String> user1 = new HashMap<>();
        user1.put("username", "ahren657");
        user1.put("hashPassword", "abcd");
        user1.put("authorityLevel", "CEO");

        HashMap<String, String> user2 = new HashMap<>();
        user1.put("username", "John");
        user1.put("hashPassword", "1234");
        user1.put("authorityLevel", "Employee");

        accounts.add(user1);
        accounts.add(user2);

    }

    @Override
    public List<HashMap<String, String>> getAccounts() {
        return accounts;
    }
}

