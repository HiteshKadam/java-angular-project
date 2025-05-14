package com.idpr.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EncryptionServiceTest {

    @InjectMocks
    @Spy
    private EncryptionService encryptionService;

    // Valid Base64-encoded 256-bit key (32 bytes)
    private static final String VALID_BASE64_KEY = "bW9ja2tleWZvcmVuY3J5cHRpb25zZXJ2aWNldGVzdA==";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(encryptionService, "baseString", VALID_BASE64_KEY);
    }

    @Test
    void decrypt_withInvalidEncryptedText_throwsRuntimeException() {
        String invalidEncryptedText = "InvalidEncryptedText";

        assertThrows(RuntimeException.class, () -> encryptionService.decrypt(invalidEncryptedText));
    }

    @Test
    void encrypt_withNullPlainText_throwsRuntimeException() {
        assertThrows(RuntimeException.class, () -> encryptionService.encrypt(null));
    }

    @Test
    void decrypt_withNullEncryptedText_throwsRuntimeException() {
        assertThrows(RuntimeException.class, () -> encryptionService.decrypt(null));
    }
}