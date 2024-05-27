package com.kn.auth.responses;

import org.springframework.dao.DataIntegrityViolationException;

import com.kn.auth.utils.StringUtil;

import io.micrometer.common.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    @Schema(name = "error", description = "Error title")
    private String error;

    @Schema(name = "errorMessage", description = "Error description message")
    private String exceptionMessage;

    @Schema(name = "column", description = "Column name where error occured")
    @Nullable
    private String column;

    public static ErrorResponse createErrorResponseFromException(Exception exception) {
    String error = "Internal Server Error";
    String exceptionMessage = exception.getMessage();
    exception.printStackTrace();
    String column = null;

    if (exception instanceof DataIntegrityViolationException) {
        error = "Data Integrity Violation Error";
        column = StringUtil.extractSubstring(exception.getMessage(), "column '(.*?)'");
    }

    return ErrorResponse.builder()
            .error(error)
            .exceptionMessage(exceptionMessage)
            .column(column)
            .build();
}
}
