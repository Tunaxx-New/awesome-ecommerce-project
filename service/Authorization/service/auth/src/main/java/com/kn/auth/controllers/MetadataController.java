package com.kn.auth.controllers;

import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @PostMapping("/metadata")
    @Operation(summary = "Getting info", description = "Return constants")
    public ResponseEntity<MetadataResponse> test() {
        return ResponseEntity.status(HttpStatus.OK).body(MetadataResponse.builder()
                .roles(detachEntities(roleRepository.findAll()))
                .categories(detachEntities(categoryRepository.findAll()))
                .paymentMethods(detachEntities(paymentMethodRepository.findAll()))
                .shippingAddresses(detachEntities(shippingAddressRepository.findAll()))
                .tags(detachEntities(tagRepository.findAll()))
                .transparentPolicies(detachEntities(transparentPolicyRepository.findAll()))
                .build());
    }

    private <T> List<T> detachEntities(List<T> entities) {
        entities.forEach(entity -> Hibernate.unproxy(entity));
        return entities;
    }
}
