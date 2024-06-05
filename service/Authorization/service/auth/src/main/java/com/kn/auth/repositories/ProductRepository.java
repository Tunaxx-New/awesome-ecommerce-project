package com.kn.auth.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.google.common.base.Optional;
import com.kn.auth.models.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAll(Pageable pageable);
    Optional<List<Product>> findAllBySellerId(int sellerId);
}
