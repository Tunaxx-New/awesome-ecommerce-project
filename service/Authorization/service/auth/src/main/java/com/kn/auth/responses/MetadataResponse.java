package com.kn.auth.responses;

import com.kn.auth.models.Authentication;
import com.kn.auth.models.Category;
import com.kn.auth.models.Order;
import com.kn.auth.models.PaymentMethod;
import com.kn.auth.models.Role;
import com.kn.auth.models.ShippingAddress;
import com.kn.auth.models.Tag;
import com.kn.auth.models.TransparentPolicy;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetadataResponse {
    @Schema(description = "Roles")
    private List<Role> roles;

    @Schema(description = "Shipping addresses")
    private List<ShippingAddress> shippingAddresses;

    @Schema(description = "Payment methods")
    private List<PaymentMethod> paymentMethods;

    @Schema(description = "Tags")
    private List<Tag> tags;

    @Schema(description = "Categories")
    private List<Category> categories;

    @Schema(description = "Transparent policies")
    private List<TransparentPolicy> transparentPolicies;
}
