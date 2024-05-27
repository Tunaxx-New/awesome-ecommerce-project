package com.kn.auth.responses;

import com.kn.auth.models.Order;
import com.kn.auth.models.OrderItem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    @Schema(description = "Order object")
    private Order order;

    @Schema(description = "Order items array")
    private List<OrderItem> orderItems;
}
