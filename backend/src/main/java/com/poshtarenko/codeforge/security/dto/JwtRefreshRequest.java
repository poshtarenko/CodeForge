package com.poshtarenko.codeforge.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class JwtRefreshRequest {
    @NotBlank String refreshToken;
}
