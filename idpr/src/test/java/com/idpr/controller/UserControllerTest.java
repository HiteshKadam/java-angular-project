package com.idpr.controller;

import com.idpr.model.User;
import com.idpr.repository.UserRepository;
import com.idpr.service.EncryptionService;
import com.idpr.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    @Spy
    private UserController userController;

    void setup() {
        userController = new UserController(userRepository, encryptionService);
    }

    @Test
    void getAllUsers_returnsDecryptedUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setAadharNumber("encryptedAadhar1");
        user1.setPanNumber("encryptedPan1");
        user1.setMobileNumber("encryptedMobile1");
        User user2 = new User();
        user2.setId(2L);
        user2.setAadharNumber("encryptedAadhar2");
        user2.setPanNumber("encryptedPan2");
        user2.setMobileNumber("encryptedMobile2");
        List<User> users = List.of(
            user1,user2
        );

        when(userRepository.findAll()).thenReturn(users);
        when(encryptionService.decrypt("encryptedAadhar1")).thenReturn("decryptedAadhar1");
        when(encryptionService.decrypt("encryptedPan1")).thenReturn("decryptedPan1");
        when(encryptionService.decrypt("encryptedMobile1")).thenReturn("decryptedMobile1");
        when(encryptionService.decrypt("encryptedAadhar2")).thenReturn("decryptedAadhar2");
        when(encryptionService.decrypt("encryptedPan2")).thenReturn("decryptedPan2");
        when(encryptionService.decrypt("encryptedMobile2")).thenReturn("decryptedMobile2");

        Object result = userController.getAllUsers();

        assertTrue(result.toString().contains("decryptedAadhar1"));
        assertTrue(result.toString().contains("decryptedPan1"));
        assertTrue(result.toString().contains("decryptedMobile1"));
        assertTrue(result.toString().contains("decryptedAadhar2"));
        assertTrue(result.toString().contains("decryptedPan2"));
        assertTrue(result.toString().contains("decryptedMobile2"));
    }

    @Test
    void getUser_withValidId_returnsDecryptedUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setAadharNumber("encryptedAadhar");
        user.setPanNumber("encryptedPan");
        user.setMobileNumber("encryptedMobile");


        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(encryptionService.decrypt("encryptedAadhar")).thenReturn("decryptedAadhar");
        when(encryptionService.decrypt("encryptedPan")).thenReturn("decryptedPan");
        when(encryptionService.decrypt("encryptedMobile")).thenReturn("decryptedMobile");

        Object result = userController.getUser(1L);
        assertNotNull(result);
    }

    @Test
    void getUser_withInvalidId_returnsErrorMessage() throws Exception {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Object result = userController.getUser(99L);

        assertEquals("Data cannot be accessed", result);
    }

    @Test
    void updateUser_withValidData_updatesUserSuccessfully() throws Exception {
        User existingUser = new User();
        existingUser.setAadharNumber("encryptedAadhar");
        existingUser.setPanNumber("encryptedPan");
        existingUser.setMobileNumber("encryptedMobile");
        existingUser.setId(1L);
        existingUser.setPassword("oldPassword");
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setAadharNumber("newAadhar");
        updatedUser.setPanNumber("newPan");
        updatedUser.setMobileNumber("newMobile");
        updatedUser.setPassword("newPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(encryptionService.encrypt("newAadhar")).thenReturn("encryptedNewAadhar");
        when(encryptionService.encrypt("newPan")).thenReturn("encryptedNewPan");
        when(encryptionService.encrypt("newMobile")).thenReturn("encryptedNewMobile");
        when(userController.getTokenDecrypt(anyString())).thenReturn("1");

        String result = userController.updateUser("1", updatedUser, "Bearer validToken");

        assertEquals("User updated successfully!", result);
        verify(userRepository).save(existingUser);
    }

    @Test
    void deleteUser_withValidId_deletesUserSuccessfully() {
        User user = new User();
        user.setId(1L);
        user.setAadharNumber("encryptedAadhar");
        user.setPanNumber("encryptedPan");
        user.setMobileNumber("encryptedMobile");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String result = userController.deleteUser(1L);

        assertEquals("User deleted successfully!", result);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_withInvalidId_returnsErrorMessage() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        String result = userController.deleteUser(99L);

        assertEquals("Data cannot be accessed", result);
    }
}