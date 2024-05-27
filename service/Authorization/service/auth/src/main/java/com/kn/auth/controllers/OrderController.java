package com.kn.auth.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kn.auth.models.Order;
import com.kn.auth.services.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Order", description = "Operations for buyers to order")
@RequestMapping("/private/buyer/order")
public class OrderController {

    private final OrderService service;

    @GetMapping("/list")
    @Operation(summary = "Getting list of orders", description = "Getting list of orders")
    public ResponseEntity<Page<Order>> list(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAll(pageable, 0));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Getting specifig of order", description = "Getting specific order")
    public ResponseEntity<Order> get(@RequestBody @RequestParam(name = "id", required = true) int orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.compose(orderId));
    }

    @PostMapping("/create")
    @Operation(summary = "Buyer creates order with their cart", description = "Making order")
    public ResponseEntity<Order> create() {
        return ResponseEntity.status(HttpStatus.OK).body(service.create(0));
    }
}
