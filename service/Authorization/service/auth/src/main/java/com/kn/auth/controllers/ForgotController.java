package com.kn.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kn.auth.Services.ForgotService;
import com.kn.auth.requests.AuthenticationRequest;

// ForgotPasswordController.java
@RestController
@RequestMapping("/forgot")
public class ForgotController {

    @Autowired
    private ForgotService forgotPasswordService;

    @PostMapping("/send")
    public ResponseEntity<String> forgotPassword(@RequestBody AuthenticationRequest request) {
        forgotPasswordService.processForgotPassword(request);
        return ResponseEntity.ok("Reset link sent to the email address.");
    }

    @PostMapping("/check")
    public ResponseEntity<String> resetPassword(@RequestBody AuthenticationRequest request) {
        //forgotPasswordService.processResetPassword(request);
        return ResponseEntity.ok("Reset link sent to the email address.");
    }
}

