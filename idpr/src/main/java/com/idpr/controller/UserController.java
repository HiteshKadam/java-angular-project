package com.idpr.controller;

import com.idpr.model.User;
import com.idpr.repository.UserRepository;
import com.idpr.service.EncryptionService;
import com.idpr.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final EncryptionService encryptionService;

    @Autowired
    private JwtUtil jwtUtil;

    public UserController(UserRepository userRepository, EncryptionService encryptionService) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
    }

    @GetMapping("/all")
    public Object getAllUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.setAadharNumber(encryptionService.decrypt(user.getAadharNumber()));
            user.setPanNumber(encryptionService.decrypt(user.getPanNumber()));
            user.setMobileNumber(encryptionService.decrypt(user.getMobileNumber()));
        }
        return new JSONObject().put("users", users).toString();
    }

    @GetMapping("/{id}")
    public Object getUser(@PathVariable Long id) throws Exception {
        try{
            validateUserAccess(id);
        } catch (SecurityException e) {
            return e.getMessage();
        }
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User decryptedUser = user.get();
            decryptedUser.setAadharNumber(encryptionService.decrypt(decryptedUser.getAadharNumber()));
            decryptedUser.setPanNumber(encryptionService.decrypt(decryptedUser.getPanNumber()));
            decryptedUser.setMobileNumber(encryptionService.decrypt(decryptedUser.getMobileNumber()));
            return decryptedUser;
        }
        return "User Data Not Found";
    }

    @PutMapping("/{id}")
    public String updateUser(
            @PathVariable String id,
            @RequestBody User updatedUser,
            @RequestHeader("Authorization") String authHeader
    ) throws Exception {

        String x = getTokenDecrypt(authHeader);
        try {
            validateUserAccess(Long.valueOf(x), updatedUser);
        } catch (SecurityException e) {
            return e.getMessage();
        }

        Optional<User> user = userRepository.findById(Long.valueOf(id));
        if (user.isPresent()) {
            User existingUser = user.get();

            // Encrypt sensitive fields before saving
            existingUser.setAadharNumber(encryptionService.encrypt(updatedUser.getAadharNumber()));
            existingUser.setPanNumber(encryptionService.encrypt(updatedUser.getPanNumber()));
            existingUser.setMobileNumber(encryptionService.encrypt(updatedUser.getMobileNumber()));

            userRepository.save(existingUser);

            return "User updated successfully!";
        }

        return "User not found!";
    }

    public String getTokenDecrypt(String authHeader) {
        log.info("Authorization Header: {}", authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "Invalid Authorization header!";
        }
        String jwtToken = authHeader.substring(7);

        log.info("JWT Token: {}", jwtToken);
        return jwtUtil.extractUserId(jwtToken).toString();
    }


    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        try{
            validateUserAccess(id);
        } catch (SecurityException e) {
            return e.getMessage();
        }
        userRepository.deleteById(id);
        return "User deleted successfully!";
    }

    private void validateUserAccess(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty() || !userId.equals(user.get().getId())) {
            throw new SecurityException("Data cannot be accessed");
        }
    }

    private void validateUserAccess(Long userId, User user) {
        Optional<User> userDb = userRepository.findById(user.getId());
        if (userDb.isEmpty()  || !userId.equals(userDb.get().getId())) {
            throw new SecurityException("Data cannot be accessed");
        }
    }
}