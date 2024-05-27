package com.kn.auth.responses;

import com.kn.auth.models.Cart;
import com.kn.auth.models.CartItem;
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
public class CartResponse {
    @Schema(description = "Cart object")
    private Cart cart;

    @Schema(description = "Cart items array")
    private List<CartItem> cartItems;
}
