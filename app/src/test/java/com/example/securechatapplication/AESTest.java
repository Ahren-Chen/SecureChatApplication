package com.example.securechatapplication;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.server.EncryptionAES.AESUtil;
import com.example.server.Request;
import com.example.server.RequestTypes;

import java.util.Base64;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class AESTest {
    @Test
    public void testPasswordEncryptionAndDecryption() {
        String plainText = "testing123.;";
        String password = "password";
        String salt = "123456789";
        IvParameterSpec ivParameterSpec = AESUtil.generateIv();
        SecretKey key = AESUtil.getKeyFromPassword(password,salt);
        String cipherText = AESUtil.encrypt(plainText, key, ivParameterSpec);
        String decryptedCipherText = AESUtil.decrypt(
                cipherText, key, ivParameterSpec);

        assertEquals(plainText, decryptedCipherText);
    }

    @Test
    public void testWithNoPasswordEncryptionAndDecryption() {
        String plainText = "`'][{}|:?><'`";
        IvParameterSpec ivParameterSpec = AESUtil.generateIv();
        SecretKey key = AESUtil.generateKey();
        String cipherText = AESUtil.encrypt(plainText, key, ivParameterSpec);
        String decryptedCipherText = AESUtil.decrypt(
                cipherText, key, ivParameterSpec);

        assertEquals(plainText, decryptedCipherText);
    }

    @Test
    public void objectEncryptionDecryptionTest() {
        Request request = new Request(RequestTypes.login, "hi");
        SecretKey key = AESUtil.generateKey();
        IvParameterSpec ivParameterSpec = AESUtil.generateIv();
        SealedObject sealedObject = AESUtil.encryptObject(
                request, key, ivParameterSpec);
        Request object = (Request) AESUtil.decryptObject(
                sealedObject, key, ivParameterSpec);
        assertEquals(request.getType(), object.getType());
        assertEquals(request.getUsername(), object.getUsername());
        assertEquals(request.getKey(), object.getKey());

    }

    @Test
    public void secretKeyToStringToSecretKeyTest() {
        SecretKey key = AESUtil.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        byte[] decodedKeyBytes = Base64.getDecoder().decode(encodedKey);
        SecretKey decodedKey = new SecretKeySpec(decodedKeyBytes, 0, decodedKeyBytes.length, "AES");

        assertEquals(key, decodedKey);
    }
}