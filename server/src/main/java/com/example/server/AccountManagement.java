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

    public int createAccount(String newName, String newPass,String auth){
        for (HashMap<String, String> h:accounts) {
            if (h.containsValue(newName)){
                System.out.println("Error: user name already in system");
                return 1;
            }
        }
        HashMap<String,String> newUser = new HashMap<>();
        newUser.put("username",newName);
        newUser.put("hashPassword",newPass);
        newUser.put("authorityLevel",auth);
        accounts.add(newUser);
        return 0;
    }
    public int deleteAccount(String name){
        for (HashMap<String, String> h:accounts) {
            if (h.containsValue(name)){
                if(!accounts.remove(h)) System.out.println("Error deleting account");
                return 0;
            }
        }
        System.out.println("Could not find account to delete");
        return 1;
    }
    public int editAccount(String oldName,String newName, String newPass, String newAuth){
        for (HashMap<String, String> h: accounts){
            if (h.containsValue(newName) && !(newName.equals(oldName))) {
                System.out.println("Name is taken by another user!");
                return 1;
            }
        }
        for (HashMap<String, String> h: accounts){
            if (h.containsValue(oldName)) {
                h.replace("username",newName);
                h.replace("hashPassword",newPass);
                h.replace("authorityLevel",newAuth);
                return 0;
            }
        }
        System.out.println("Error updating user info");
        return 1;
    }
}

