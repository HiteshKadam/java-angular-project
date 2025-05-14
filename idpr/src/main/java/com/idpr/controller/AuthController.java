package com.idpr.controller;

import com.idpr.model.JwtRequest;
import com.idpr.model.JwtResponse;
import com.idpr.model.User;
import com.idpr.repository.UserRepository;
import com.idpr.service.CustomUserDetailsService;
import com.idpr.service.EncryptionService;
import com.idpr.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EncryptionService encryptionService;

    @PostMapping("/register")
    public String register(@RequestBody User user) throws Exception {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAadharNumber(encryptionService.encrypt(user.getAadharNumber()));
        user.setPanNumber(encryptionService.encrypt(user.getPanNumber()));
        user.setMobileNumber(encryptionService.encrypt(user.getMobileNumber()));
        user.setUsername(user.getUsername());
        userRepository.save(user);
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody JwtRequest jwtRequest) throws Exception {
        Optional<User> userOptional = userRepository.findByUsername(jwtRequest.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(jwtRequest.getPassword(), user.getPassword())) {
                final String jwt = jwtUtil.generateToken(user.getUsername(),user.getId());
                return new JwtResponse(jwt);
            } else {
                log.error("Invalid password for user: {}", jwtRequest.getUsername());
                throw new Exception("Invalid username or password");
            }
        } else {
            log.error("User not found: {}", jwtRequest.getUsername());
            throw new Exception("Invalid username or password");
        }
    }
}