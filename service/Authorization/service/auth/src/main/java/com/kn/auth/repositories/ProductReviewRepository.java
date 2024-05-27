package com.kn.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kn.auth.models.ProductReview;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer> {
}
