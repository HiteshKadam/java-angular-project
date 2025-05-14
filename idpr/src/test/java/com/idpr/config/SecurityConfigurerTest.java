package com.idpr.config;

import com.idpr.filter.JwtRequestFilter;
import com.idpr.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SecurityConfigurerTest {

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private HttpSecurity httpSecurity;

    @Mock
    private SecurityFilterChain securityFilterChain;

    @InjectMocks
    @Spy
    private SecurityConfigurer securityConfigurer;

    @Test
    void authenticationManager_withNullConfiguration_throwsException() {
        assertThrows(NullPointerException.class, () -> securityConfigurer.authenticationManager(null));
    }


    @Test
    void passwordEncoder_returnsBCryptPasswordEncoderInstance() {
        PasswordEncoder passwordEncoder = securityConfigurer.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

}