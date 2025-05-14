package com.idpr.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private String validToken;
    private String expiredToken;

    @BeforeEach
    void setUp() {
        validToken = jwtUtil.generateToken("testUser", 1L);
        expiredToken = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 3))
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, "secret")
                .compact();
    }

    @Test
    void extractUsername_withValidToken_returnsUsername() {
        String username = jwtUtil.extractUsername(validToken);
        assertEquals("testUser", username);
    }

    @Test
    void extractUserId_withValidToken_returnsUserId() {
        Long userId = jwtUtil.extractUserId(validToken);
        assertEquals(1L, userId);
    }

    @Test
    void extractExpiration_withValidToken_returnsExpirationDate() {
        Date expiration = jwtUtil.extractExpiration(validToken);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void isTokenExpired_withExpiredToken_returnsTrue() {
        assertThrows(ExpiredJwtException.class, () -> {
            jwtUtil.isTokenExpired(expiredToken);
        });
    }

    @Test
    void isTokenExpired_withValidToken_returnsFalse() {
        assertFalse(jwtUtil.isTokenExpired(validToken));
    }

    @Test
    void validateToken_withValidTokenAndMatchingUsername_returnsTrue() {
        assertTrue(jwtUtil.validateToken(validToken, "testUser"));
    }

    @Test
    void validateToken_withValidTokenAndNonMatchingUsername_returnsFalse() {
        assertFalse(jwtUtil.validateToken(validToken, "wrongUser"));
    }

    @Test
    void validateToken_withExpiredToken_returnsFalse() {
        assertThrows(ExpiredJwtException.class, () -> {
            jwtUtil.validateToken(expiredToken, "testUser");
        });
    }

    @Test
    void generateToken_withValidInputs_createsValidToken() {
        String token = jwtUtil.generateToken("newUser", 2L);
        assertNotNull(token);
        assertEquals("newUser", jwtUtil.extractUsername(token));
        assertEquals(2L, jwtUtil.extractUserId(token));
    }
}