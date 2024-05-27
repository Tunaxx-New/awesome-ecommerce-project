package com.kn.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kn.auth.constants.TableNameConstants;
import com.kn.auth.models.CartItem;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    @Query(value = "SELECT ct.* FROM " + TableNameConstants.CART_ITEM + " ct "
            + "JOIN " + TableNameConstants.CART + " c ON ct.cart_id = c.id "
            + "JOIN " + TableNameConstants.BUYERS + " b ON c.buyer_id = b.id "
            + "JOIN " + TableNameConstants.AUTHENTICATION + " a ON b.authentication_id = a.id "
            + "WHERE a.id = :authenticationId", nativeQuery = true)
    Optional<List<CartItem>> findAllByAuthenticationId(@Param("authenticationId") int authentication_id);

    Optional<List<CartItem>> findAllByCartId(int cartId);

    void deleteAllByCartId(int cartId);

    Optional<CartItem> findByProductId(int productId);
}
