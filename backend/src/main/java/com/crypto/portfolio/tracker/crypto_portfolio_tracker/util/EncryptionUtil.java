package com.crypto.portfolio.tracker.crypto_portfolio_tracker.util;

import javax.crypto.Cipher;
import javax.crypto.spec.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionUtil {

    private static final String ALGO = "AES/GCM/NoPadding";
    private static final int AES_KEY_SIZE = 32;
    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH = 128;

    // reads from env var, falls back to default 32-char key
    private static final String SECRET = System.getenv("APP_ENCRYPTION_KEY") != null
            ? System.getenv("APP_ENCRYPTION_KEY")
            : "0123456789ABCDEF0123456789ABCDEF";

    private static byte[] generateIV() {
        SecureRandom rnd = new SecureRandom();
        byte[] iv = new byte[IV_LENGTH];
        rnd.nextBytes(iv);
        return iv;
    }

    private static SecretKeySpec getKey() throws Exception {
        byte[] keyBytes = SECRET.getBytes(StandardCharsets.US_ASCII);
        if (keyBytes.length != AES_KEY_SIZE) {
            throw new Exception("AES key must be exactly 32 bytes for AES-256. Current length: " + keyBytes.length);
        }
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static String encrypt(String data) throws Exception {
        byte[] iv = generateIV();
        SecretKeySpec key = getKey();
        Cipher cipher = Cipher.getInstance(ALGO);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        byte[] full = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, full, 0, iv.length);
        System.arraycopy(encrypted, 0, full, iv.length, encrypted.length);
        return Base64.getEncoder().encodeToString(full);
    }

    public static String decrypt(String data) throws Exception {
        byte[] full = Base64.getDecoder().decode(data);
        byte[] iv = new byte[IV_LENGTH];
        System.arraycopy(full, 0, iv, 0, IV_LENGTH);
        byte[] cipherText = new byte[full.length - IV_LENGTH];
        System.arraycopy(full, IV_LENGTH, cipherText, 0, cipherText.length);
        SecretKeySpec key = getKey();
        Cipher cipher = Cipher.getInstance(ALGO);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] decrypted = cipher.doFinal(cipherText);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}