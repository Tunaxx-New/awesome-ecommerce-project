package com.kn.auth.models;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Embeddable
@AllArgsConstructor
public class TimeValue {
    private LocalDateTime dateTime;
    private BigDecimal value;
}
