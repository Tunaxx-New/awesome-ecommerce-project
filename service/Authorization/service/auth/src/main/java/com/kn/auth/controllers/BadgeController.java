package com.kn.auth.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kn.auth.models.Badge;
import com.kn.auth.services.BadgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/private/badges")
@Tag(name = "Badge", description = "Badge controller")
public class BadgeController {

    private final BadgeService service;

    @GetMapping("/test")
    @Operation(summary = "Getting profile badges", description = "Return account badges")
    public ResponseEntity<Badge> test() {
        return ResponseEntity.status(HttpStatus.OK).body(service.test());
    }
}
