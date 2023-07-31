package com.poshtarenko.codeforge.service;

import com.poshtarenko.codeforge.security.pojo.JwtRefreshRequest;
import com.poshtarenko.codeforge.security.pojo.JwtResponse;

public interface RefreshTokenService {
    String createToken(long userId);

    JwtResponse refreshToken(JwtRefreshRequest refreshRequest);
}
