package com.kn.auth.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kn.auth.models.Seller;
import com.kn.auth.models.TransparentPolicy;
import com.kn.auth.responses.ErrorResponse;
import com.kn.auth.responses.ResponseWrapper;
import com.kn.auth.services.SellerService;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/private")
@Tag(name = "Seller", description = "Operations for sellers and one for register as seller")
public class SellerController {

    private final SellerService sellerService;

    @PostMapping(value = "/registerSeller", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register seller account", description = "Register seller account to authorized user")
    public ResponseEntity<ResponseWrapper<Seller>> register(@RequestBody RequestEntity<Seller> seller) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseWrapper<Seller>(sellerService.create(seller.getBody(), 0), null));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseWrapper<Seller>(null, ErrorResponse.createErrorResponseFromException(e)));
        }
    }

    @GetMapping("/seller/profile")
    @Operation(summary = "Profile info", description = "Returns seller profile info object")
    public ResponseEntity<Seller> profile() {
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.get(0));
    }

    @PostMapping("/seller/profile/change")
    @Operation(summary = "Change seller profile info", description = "Changes seller profile")
    public ResponseEntity<Seller> changeProfile(@RequestBody RequestEntity<Seller> changedSeller) {
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.update(changedSeller.getBody(), 0));
    }

    @GetMapping("/seller/profile/loyalty")
    @Operation(summary = "Loyalty of profile", description = "Return key feature gamified statistics")
    public ResponseEntity<BigDecimal> getProfileLoyalty() {
        return ResponseEntity.status(HttpStatus.OK).body(sellerService.getLoyaltyIndex(0));
    }
}
