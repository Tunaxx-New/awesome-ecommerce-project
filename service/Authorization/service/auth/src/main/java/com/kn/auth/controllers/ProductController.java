package com.kn.auth.controllers;

import java.util.List;
import java.util.ArrayList;

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

import com.kn.auth.models.Category;
import com.kn.auth.models.Order;
import com.kn.auth.models.Product;
import com.kn.auth.models.ProductReview;
import com.kn.auth.repositories.CategoryRepository;
import com.kn.auth.repositories.TagRepository;
import com.kn.auth.services.ProductService;
import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product", description = "Operations for sellers and one for register as seller")
public class ProductController {

    private final ProductService service;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @GetMapping("/public/product/list")
    @Operation(summary = "Getting list of products", description = "Getting list of products")
    public ResponseEntity<Page<Product>> list(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAll(pageable));
    }

    @GetMapping("/public/product/listByTag")
    @Operation(summary = "Getting list of products by tag name", description = "Getting list of products with tag with divider ','")
    public ResponseEntity<Page<Product>> getAllByTag(@PageableDefault(size = 10) Pageable pageable, String tagsString) {
        List<com.kn.auth.models.Tag> tags = new ArrayList<>();
        for (String tagString : tagsString.split(","))
            tags.add(tagRepository.findByTitle(tagString).get());
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllByTag(tags, pageable));
    }

    @GetMapping("/public/product/listByCategory")
    @Operation(summary = "Getting list of products by category name", description = "Getting list of products with category")
    public ResponseEntity<Page<Product>> getAllByCategory(@PageableDefault(size = 10) Pageable pageable,
            String categoriesString) {
        List<Category> categories = new ArrayList<>();
        for (String categoryString : categoriesString.split(","))
            categories.add(categoryRepository.findByName(categoryString).get());
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllByCategory(categories, pageable));
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
