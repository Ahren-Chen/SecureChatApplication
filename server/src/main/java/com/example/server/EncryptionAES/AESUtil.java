package com.example.server.EncryptionAES;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    //This should be randomly generated and passed from MAP to KDC for encrypt or decrypt
    //Hard setting for now, to fix later (OPTIONAL)
    public static IvParameterSpec iv = new IvParameterSpec(new byte[]{ 0, 1, 0, 2, 0, 3, 0, 4, 0, 5, 0, 6, 0, 7, 0, 8 });

    public static SecretKey generateKey() {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    public static SecretKey getKeyFromPassword(String password, String salt) {
        SecretKeyFactory factory;

        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);

        try {
            return new SecretKeySpec(factory.generateSecret(spec)
                    .getEncoded(), "AES");
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static String encrypt(String input, SecretKey key) {

        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        byte[] cipherText;
        try {
            cipherText = cipher.doFinal(input.getBytes());
        } catch(BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder()
                .encodeToString(cipherText);

    }

    public static String decrypt(String cipherText, SecretKey key){
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        byte[] plainText;
        try {
            plainText = cipher.doFinal(Base64.getDecoder()
                    .decode(cipherText));
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
        return new String(plainText);
    }

    public static SealedObject encryptObject(Serializable object,
                                             SecretKey key) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            System.out.println("IV: " + iv);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        try {
            return new SealedObject(object, cipher);
        } catch (IOException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    public static Serializable decryptObject(SealedObject sealedObject,
                                             SecretKey key){
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        Serializable unsealedObject;
        try {
            System.out.println("IV decrypted: " + iv);
            unsealedObject = (Serializable) sealedObject.getObject(cipher);
        } catch (IOException | IllegalBlockSizeException | ClassNotFoundException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return unsealedObject;
    }
}
