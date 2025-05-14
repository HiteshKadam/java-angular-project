package com.idpr.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/info")
public class InfoController {

    @GetMapping("/security")
    public String getSecurityInfo() {
        return "Data security is crucial. Use strong passwords, enable two-factor authentication, and encrypt sensitive data.";
    }

    @GetMapping("/idpr")
    public String getIdprInfo() {
        return "IDPR ensures proper identity protection and regulation for secure digital interactions.";
    }
}
