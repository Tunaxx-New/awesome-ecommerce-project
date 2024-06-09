package com.kn.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.google.common.base.Optional;
import com.kn.auth.models.Category;
import com.kn.auth.models.ShippingAddress;
import com.kn.auth.models.Tag;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);
}
