package com.example.securechatapplication;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.securechatapplication.EncryptionAES.AESUtil;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

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
        String plainText = "testing123.;";
        IvParameterSpec ivParameterSpec = AESUtil.generateIv();
        SecretKey key = AESUtil.generateKey();
        String cipherText = AESUtil.encrypt(plainText, key, ivParameterSpec);
        String decryptedCipherText = AESUtil.decrypt(
                cipherText, key, ivParameterSpec);

        assertEquals(plainText, decryptedCipherText);
    }
}