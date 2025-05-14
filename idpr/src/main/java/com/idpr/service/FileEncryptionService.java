package com.idpr.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class FileEncryptionService {

    @Value("${encyptionkey}")
    private String baseString;
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 12;
    private static final int TAG_SIZE = 128;
    private static final String KEY_FILE = "encryption.key"; // Path to store the key

    private SecretKey secretKey;

    @PostConstruct
    void init() {
        try {
            if (Files.exists(Paths.get(KEY_FILE))) {
                // Load existing key
                String base64Key = baseString;
                byte[] decodedKey = Base64.getDecoder().decode(base64Key);
                secretKey = new SecretKeySpec(decodedKey, "AES");
            } else {
                // Generate a new key and store it
                String base64Key = baseString;
                byte[] decodedKey = Base64.getDecoder().decode(base64Key);
                secretKey = new SecretKeySpec(decodedKey, "AES");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize encryption key", e);
        }
    }

    public byte[] encryptFile(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(TAG_SIZE, iv));
        byte[] encryptedData = cipher.doFinal(data);

        // Combine IV and encrypted data
        byte[] combinedData = new byte[IV_SIZE + encryptedData.length];
        System.arraycopy(iv, 0, combinedData, 0, IV_SIZE);
        System.arraycopy(encryptedData, 0, combinedData, IV_SIZE, encryptedData.length);

        return combinedData;
    }

    public byte[] decryptFile(byte[] encryptedData) throws Exception {
        byte[] iv = new byte[IV_SIZE];
        System.arraycopy(encryptedData, 0, iv, 0, IV_SIZE);
        byte[] cipherText = new byte[encryptedData.length - IV_SIZE];
        System.arraycopy(encryptedData, IV_SIZE, cipherText, 0, cipherText.length);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(TAG_SIZE, iv));

        return cipher.doFinal(cipherText);
    }
}
