package com.kn.auth.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kn.auth.models.Cart;
import com.kn.auth.models.CartItem;
import com.kn.auth.services.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/private/buyer/cart")
@Tag(name = "Cart", description = "Cart operations")
public class CartController {

    private final CartService service;

    @GetMapping("/")
    @Operation(summary = "Cart info", description = "Returns buyer's cart with items")
    public ResponseEntity<Cart> info() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getComposed(0));
    }

    @PostMapping("/add")
    @Operation(summary = "Add product to cart", description = "Add product to cart")
    public ResponseEntity<CartItem> add(@RequestParam(name = "id", required = true) int productId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.add(productId, 0));
    }

    @PostMapping("/change")
    @Operation(summary = "Change cart containtment", description = "Changes cart containtment with amounts or deleted objects")
    public ResponseEntity<List<CartItem>> change(@RequestBody RequestEntity<List<CartItem>> cartItems) {
        return ResponseEntity.status(HttpStatus.OK).body(service.changeItems(cartItems.getBody(), 0));
    }

    @PostMapping("/clear")
    @Operation(summary = "Clear cart items", description = "Clears items from cart")
    public ResponseEntity<Boolean> clear() {
        return ResponseEntity.status(HttpStatus.OK).body(service.clear(0));
    }

    @PostMapping("/changeCart")
    @Operation(summary = "Changes cart", description = "Changes cart's safe to update values")
    public ResponseEntity<Cart> changeShippingAddress(@RequestBody RequestEntity<Cart> cart) {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(cart.getBody(), 0));
    }
}
