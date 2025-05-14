package com.idpr.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class FileEncryptionServiceTest {

    @InjectMocks
    @Spy
    private FileEncryptionService fileEncryptionService;

    private static final String VALID_BASE64_KEY = "bW9ja2tleWZvcmVuY3J5cHRpb25zZXJ2aWNldGVzdA==";

    @BeforeEach
    void setUp() {
        // Decode the base64 key and set the secretKey field directly
        byte[] decodedKey = Base64.getDecoder().decode(VALID_BASE64_KEY);
        SecretKey secretKey = new SecretKeySpec(decodedKey, "AES");
        ReflectionTestUtils.setField(fileEncryptionService, "secretKey", secretKey);
    }

    @Test
    void encryptFile_withNullData_throwsException() {
        assertThrows(InvalidKeyException.class, () -> fileEncryptionService.encryptFile(null));
    }

    @Test
    void decryptFile_withEmptyEncryptedData_throwsException() {
        byte[] encryptedData = new byte[0];
        assertThrows(Exception.class, () -> fileEncryptionService.decryptFile(encryptedData));
    }
}