package com.poshtarenko.codeforge.security.pojo;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class JwtRefreshRequest {
    @NotBlank String refreshToken;
}
