package com.idpr.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class ApiController {
    @GetMapping("/message")
    public ResponseEntity<String> getMessage() {
        return new ResponseEntity<>("Hello from Spring Boot Backend!", HttpStatus.OK);
    }
}
