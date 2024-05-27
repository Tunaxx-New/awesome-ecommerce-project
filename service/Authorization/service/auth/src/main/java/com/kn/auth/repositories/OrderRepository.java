package com.kn.auth.repositories;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kn.auth.constants.TableNameConstants;
import com.kn.auth.models.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findAllByBuyerId(int buyerId, Pageable pageable);

    @Query(value = "SELECT or.* FROM " + TableNameConstants.ORDER + " or "
            + "JOIN " + TableNameConstants.BUYERS + " b ON or.buyer_id = b.id "
            + "JOIN " + TableNameConstants.AUTHENTICATION + " a ON b.authentication_id = a.id "
            + "WHERE a.id = :authenticationId", nativeQuery = true)
    Optional<Order> findByAuthenticationId(@Param("authenticationId") int authenticationId);

    @Query(value = "SELECT AVG(o.:column) FROM " + TableNameConstants.ORDER
            + " o WHERE e.:filterColumn = :filterValue", nativeQuery = true)
    Optional<Double> getAverage(String column, String filterColumn, String filterValue);
}
