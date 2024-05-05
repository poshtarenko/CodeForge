package com.poshtarenko.codeforge.service;

import com.poshtarenko.codeforge.security.dto.JwtRefreshRequest;
import com.poshtarenko.codeforge.security.dto.JwtResponse;

public interface RefreshTokenService {
    String createToken(long userId);

    JwtResponse refreshToken(JwtRefreshRequest refreshRequest);
}
