package com.idpr.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ApiControllerTest {

    @InjectMocks
    private ApiController apiController;

    @Test
    void getMessage_returnsHelloMessage() {
        ResponseEntity<String> response = apiController.getMessage();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hello from Spring Boot Backend!", response.getBody());
    }

    @Test
    void getMessage_returnsNonNullResponse() {
        ResponseEntity<String> response = apiController.getMessage();
        assertNotNull(response);
        assertNotNull(response.getBody());
    }
}