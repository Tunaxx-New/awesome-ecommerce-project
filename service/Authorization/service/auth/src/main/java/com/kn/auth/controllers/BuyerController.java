package com.kn.auth.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import com.kn.auth.models.Buyer;
import com.kn.auth.responses.ClvResponse;
import com.kn.auth.services.BuyerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/private/buyer")
@Tag(name = "Buyer", description = "Buyer operations")
public class BuyerController {

    private final BuyerService service;

    @GetMapping("/profile")
    @Operation(summary = "Profile info", description = "Returns buyer profile info object")
    public ResponseEntity<Buyer> profile() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getByAuthenticationIdSafe(0));
    }

    @PostMapping("/profile/change")
    @Operation(summary = "Change profile info", description = "Changes buyer profile")
    public ResponseEntity<Buyer> changeProfile(@RequestBody RequestEntity<Buyer> updatedBuyer) {
        return ResponseEntity.status(HttpStatus.OK).body(service.safeUpdate(updatedBuyer.getBody(), 0));
    }

    @GetMapping("/profile/loyalty")
    @Operation(summary = "Loyalty of profile", description = "Return key feature gamified statistics")
    public ResponseEntity<BigDecimal> getProfileLoyalty() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getLoyaltyIndexAuthentication(0));
    }

    @GetMapping("/profile/CLV")
    @Operation(summary = "CLV of profile", description = "Return users CLV")
    public ResponseEntity<ClvResponse> getCLVMetrics() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getCLVs(0));
    }
}
