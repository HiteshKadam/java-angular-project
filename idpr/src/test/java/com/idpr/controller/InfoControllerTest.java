package com.idpr.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class InfoControllerTest {

    @InjectMocks
    private InfoController infoController;

    @Test
    void getSecurityInfo_returnsSecurityMessage() {
        String result = infoController.getSecurityInfo();
        assertEquals("Data security is crucial. Use strong passwords, enable two-factor authentication, and encrypt sensitive data.", result);
    }

    @Test
    void getIdprInfo_returnsIdprMessage() {
        String result = infoController.getIdprInfo();
        assertEquals("IDPR ensures proper identity protection and regulation for secure digital interactions.", result);
    }
}