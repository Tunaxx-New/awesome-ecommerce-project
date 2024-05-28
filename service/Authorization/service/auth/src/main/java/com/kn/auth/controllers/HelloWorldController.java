package com.kn.auth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Hello world test endpoints", description = "Test of availability")
public class HelloWorldController {
    @GetMapping("/public/hello-world!")
    public ResponseEntity<String> publicHello() {
        return ResponseEntity.ok("Hello, world! Public");
    }

    @GetMapping("/private/hello-world!")
    public ResponseEntity<String> privateHello() {
        return ResponseEntity.ok("Hello, world! Private");
    }
}
