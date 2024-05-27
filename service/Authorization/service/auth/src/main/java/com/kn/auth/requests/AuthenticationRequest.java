package com.kn.auth.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @Schema(name = "email", example = "test@gmail.com", required = true)
    private String email;

    @Schema(name = "password", example = "Tunaxx2024!", required = true)
    private String password;
}
