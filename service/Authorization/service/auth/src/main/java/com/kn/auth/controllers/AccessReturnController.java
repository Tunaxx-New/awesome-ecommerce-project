package com.kn.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kn.auth.requests.AuthenticationRequest;
import com.kn.auth.services.AccessReturnService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/public/forgot")
@Tag(name = "Authentication", description = "Forgot password endpoints for public")
public class AccessReturnController {

    @Autowired
    private AccessReturnService forgotPasswordService;

    @PostMapping("/send")
    public ResponseEntity<String> forgotPassword(@RequestBody AuthenticationRequest request) {
        forgotPasswordService.processForgotPassword(request);
        return ResponseEntity.ok("Reset link sent to the email address.");
    }

    @PostMapping("/check")
    public ResponseEntity<String> resetPassword(@RequestBody AuthenticationRequest request) {
        // forgotPasswordService.processResetPassword(request);
        return ResponseEntity.ok("Reset link sent to the email address.");
    }
}
