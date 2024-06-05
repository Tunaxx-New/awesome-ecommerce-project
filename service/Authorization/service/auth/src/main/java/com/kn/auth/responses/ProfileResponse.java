package com.kn.auth.responses;

import com.kn.auth.models.Authentication;
import com.kn.auth.models.Order;
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
public class ProfileResponse {
    @Schema(description = "Orders list")
    private List<Order> orders;

    @Schema(description = "Order items array")
    private Authentication authentication;
}
