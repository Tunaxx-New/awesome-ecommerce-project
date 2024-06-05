package com.kn.auth.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kn.auth.models.Order;
import com.kn.auth.models.Product;
import com.kn.auth.models.ProductReview;
import com.kn.auth.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product", description = "Operations for sellers and one for register as seller")
public class ProductController {

    private final ProductService service;

    @GetMapping("/public/product/list")
    @Operation(summary = "Getting list of products", description = "Getting list of products")
    public ResponseEntity<Page<Product>> list(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAll(pageable));
    }

    @GetMapping("/public/product/")
    @Operation(summary = "Getting list of products", description = "Getting specific product")
    public ResponseEntity<Product> get(@RequestParam(name = "id", required = true) int productId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.get(productId));
    }

    @PostMapping("/private/seller/product/add")
    @Operation(summary = "Seller creates product", description = "Creating product on marketplace")
    public ResponseEntity<Product> add(@RequestBody RequestEntity<Product> product) {
        return ResponseEntity.status(HttpStatus.OK).body(service.create(product.getBody(), 0));
    }

    @PostMapping("/private/buyer/product/id/review")
    @Operation(summary = "Make review on product", description = "Checks if buyer reviewed product and add review")
    public ResponseEntity<ProductReview> reviewOrder(@RequestBody RequestEntity<ProductReview> review,
            @RequestParam(name = "id", required = true) int productId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.review(review.getBody(), productId, 0));
    }

    @GetMapping("/private/buyer/product/id/orders")
    @Operation(summary = "Getting transparency order history", description = "Returns orders history that accepted by seller transparency policy")
    public ResponseEntity<List<Order>> orders(@RequestParam(name = "id", required = true) int productId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.orders(productId));
    }
}
