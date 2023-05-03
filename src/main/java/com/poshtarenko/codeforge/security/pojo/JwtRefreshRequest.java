package com.poshtarenko.codeforge.security.pojo;

import lombok.Value;

@Value
public class JwtRefreshRequest {
    String refreshToken;
}
