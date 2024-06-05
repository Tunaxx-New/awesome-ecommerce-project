package com.kn.auth.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kn.auth.repositories.CategoryRepository;
import com.kn.auth.repositories.PaymentMethodRepository;
import com.kn.auth.repositories.RoleRepository;
import com.kn.auth.repositories.ShippingAddressRepository;
import com.kn.auth.repositories.TagRepository;
import com.kn.auth.repositories.TransparentPolicyRepository;
import com.kn.auth.responses.MetadataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
@Tag(name = "Metadata", description = "Info controller")
public class MetadataController {

    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final TagRepository tagRepository;
    private final TransparentPolicyRepository transparentPolicyRepository;

    @GetMapping("/metadata")
    @Operation(summary = "Getting info", description = "Return constants")
    public ResponseEntity<MetadataResponse> test() {
        return ResponseEntity.status(HttpStatus.OK).body(MetadataResponse.builder()
                .roles(roleRepository.findAll())
                .categories(categoryRepository.findAll())
                .paymentMethods(paymentMethodRepository.findAll())
                .shippingAddresses(shippingAddressRepository.findAll())
                .tags(tagRepository.findAll())
                .transparentPolicies(transparentPolicyRepository.findAll())
                .build());
    }
}
