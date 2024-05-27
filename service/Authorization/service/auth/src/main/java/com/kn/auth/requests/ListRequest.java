package com.kn.auth.requests;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListRequest<T> {
    @Schema(name = "List []", example = "[1, 2, 3]", required = true)
    private List<T> list;
}
