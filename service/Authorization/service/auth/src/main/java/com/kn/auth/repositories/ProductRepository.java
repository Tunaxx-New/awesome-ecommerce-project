package com.kn.auth.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import com.google.common.base.Optional;
import com.kn.auth.constants.TableNameConstants;
import com.kn.auth.models.Tag;
import com.kn.auth.models.Category;
import com.kn.auth.models.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAll(Pageable pageable);

    Optional<List<Product>> findAllBySellerId(int sellerId);

    //@Query("SELECT p FROM Product p JOIN p.categories c WHERE c.name = :categoryName")
    //Page<Product> findAllByCategory(@Param("categoryName") String categoryName, Pageable pageable);

    //@Query("SELECT p FROM Product p JOIN p.tags c WHERE c.title = :tagName")
    //Page<Product> findAllByTag(@Param("tagName") String tagName, Pageable pageable);

    Page<Product> findByCategoriesIn(List<Category> categories, Pageable pageable);
    Page<Product> findByTagsIn(List<Tag> tags, Pageable pageable);
}
