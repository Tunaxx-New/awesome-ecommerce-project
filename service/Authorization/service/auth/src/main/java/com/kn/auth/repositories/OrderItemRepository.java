package com.kn.auth.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kn.auth.constants.TableNameConstants;
import com.kn.auth.models.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    Optional<List<OrderItem>> findAllByOrderId(int orderId);

    @Query(value = "SELECT AVG(o.:column) FROM " + TableNameConstants.ORDER_ITEM
            + " o WHERE e.:filterColumn = :filterValue", nativeQuery = true)
    Optional<Double> getAverage(String column, String filterColumn, String filterValue);
    Optional<List<OrderItem>> findAllByProductId(int productId);
}
