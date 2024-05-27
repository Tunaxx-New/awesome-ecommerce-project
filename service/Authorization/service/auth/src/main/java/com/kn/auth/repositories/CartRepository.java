package com.kn.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kn.auth.constants.TableNameConstants;
import com.kn.auth.models.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    @Query(value = "SELECT c.* FROM " + TableNameConstants.CART + " c "
            + "JOIN " + TableNameConstants.BUYERS + " b ON c.buyer_id = b.id "
            + "JOIN " + TableNameConstants.AUTHENTICATION + " a ON b.authentication_id = a.id "
            + "WHERE a.id = :authenticationId", nativeQuery = true)
    Optional<Cart> findByAuthenticationId(@Param("authenticationId") int authenticationId);
}
