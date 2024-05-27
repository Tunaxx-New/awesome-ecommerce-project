package com.kn.auth.responses;

import java.util.List;
import java.util.ArrayList;

import com.kn.auth.models.TimeValue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ClvResponse {
    @Builder.Default
    @Schema(description = "Calculated CLV with time and delta values")
    private List<TimeValue> CLVsGapsDelta = new ArrayList<>();

    @Builder.Default
    @Schema(description = "Calculated CLV with time and static values")
    private List<TimeValue> CLVsGapsStatic = new ArrayList<>();

    @Schema(description = "Calculated CLV overall")
    private BigDecimal CLVsAverage;

}
