package com.kn.auth.requests;

import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageableRequest {
    @Schema(name = "sort")
    private SortProperties sortProperties;
    @Schema(name = "pageable")
    private Pageable pageable;

    public PageRequest getPageRequest() {
        Sort sort = Sort.by(
                sortProperties.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                sortProperties.getSortBy());
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }
}
