package com.kn.auth.responses;

import java.util.List;

import com.kn.auth.models.Badge;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class LoyaltyResponse {
    @Schema(description = "Calculated loyalty value")
    private int loyaltyValue;

    @Schema(description = "List of badges")
    private List<Badge> badges;
}
