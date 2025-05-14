package com.idpr.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptionService {

    @Value("${encyptionkey}")
    private String baseString;
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int KEY_SIZE = 256; // 256 bits key
    private static final int IV_SIZE = 12;   // 96 bits, recommended for GCM
    private static final int TAG_SIZE = 128; // 128 bits authentication tag

    private SecretKey secretKey;

    @PostConstruct
    void init() {
        // Replace this with secure key generation/storage in production
        try {
            // Hardcoded key for demo/testing
            // Length = 32 bytes for 256 bits
            String base64Key = baseString;
            byte[] decodedKey = Base64.getDecoder().decode(base64Key);
            secretKey = new SecretKeySpec(decodedKey, "AES");


            // Validate key length
            if (decodedKey.length * 8 != KEY_SIZE) {
                throw new IllegalArgumentException("Key length must be 256 bits");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize AES key", e);
        }
    }

    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            // Generate a random IV
            byte[] iv = new byte[IV_SIZE];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);

            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_SIZE, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            // Combine IV and cipherText for storage/transmission
            byte[] encryptedData = new byte[IV_SIZE + cipherText.length];
            System.arraycopy(iv, 0, encryptedData, 0, IV_SIZE);
            System.arraycopy(cipherText, 0, encryptedData, IV_SIZE, cipherText.length);

            return Base64.getEncoder().encodeToString(encryptedData);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred during encryption", e);
        }
    }

    public String decrypt(String encryptedText) {
        try {
            byte[] encryptedData = Base64.getDecoder().decode(encryptedText);

            // Extract IV
            byte[] iv = new byte[IV_SIZE];
            System.arraycopy(encryptedData, 0, iv, 0, IV_SIZE);

            // Extract cipherText
            int cipherTextLength = encryptedData.length - IV_SIZE;
            byte[] cipherText = new byte[cipherTextLength];
            System.arraycopy(encryptedData, IV_SIZE, cipherText, 0, cipherTextLength);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_SIZE, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            byte[] plainTextBytes = cipher.doFinal(cipherText);

            return new String(plainTextBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Error occurred during decryption", e);
        }
    }
}