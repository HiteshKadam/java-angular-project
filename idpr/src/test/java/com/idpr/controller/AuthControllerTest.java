package com.idpr.controller;

import com.idpr.model.JwtRequest;
import com.idpr.model.JwtResponse;
import com.idpr.model.User;
import com.idpr.repository.UserRepository;
import com.idpr.service.CustomUserDetailsService;
import com.idpr.service.EncryptionService;
import com.idpr.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EncryptionService encryptionService;

    @InjectMocks
    @Spy
    private AuthController authController;

    @Test
    void register_withValidUser_registersSuccessfully() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setAadharNumber("123456789012");
        user.setPanNumber("ABCDE1234F");
        user.setMobileNumber("9876543210");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(encryptionService.encrypt("123456789012")).thenReturn("encryptedAadhar");
        when(encryptionService.encrypt("ABCDE1234F")).thenReturn("encryptedPan");
        when(encryptionService.encrypt("9876543210")).thenReturn("encryptedMobile");

        String response = authController.register(user);

        assertEquals("User registered successfully!", response);
        verify(userRepository).save(user);
    }

    @Test
    void register_withNullUser_throwsException() {
        assertThrows(Exception.class, () -> authController.register(null));
    }

    @Test
    void login_withValidCredentials_returnsJwtResponse() throws Exception {
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setUsername("testUser");
        jwtRequest.setPassword("password");
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("encodedPassword");
        user.setId(1L);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("testUser", 1L)).thenReturn("validJwtToken");

        JwtResponse response = authController.login(jwtRequest);

        assertEquals("validJwtToken", response.getJwtToken());
    }

    @Test
    void login_withInvalidPassword_throwsException() {
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setUsername("testUser");
        jwtRequest.setPassword("wrongPassword");
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(Exception.class, () -> authController.login(jwtRequest));
    }

    @Test
    void login_withNonExistentUser_throwsException() {
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setUsername("nonExistentUser");
        jwtRequest.setPassword("password");

        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> authController.login(jwtRequest));
    }
}