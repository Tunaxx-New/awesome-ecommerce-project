package com.kn.auth.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SortProperties {
    @Schema(name = "pageNumber", example = "1", description = "Column name to sort by", required = true)
    private String sortBy;

    @Schema(name = "sortOrder", example = "ASC", description = "Ascending = ASC, Descending = *something else*", required = true)
    private String sortOrder;
}
