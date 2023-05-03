package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.entity.RefreshToken;
import com.poshtarenko.codeforge.entity.User;
import com.poshtarenko.codeforge.repository.RefreshTokenRepository;
import com.poshtarenko.codeforge.security.jwt.JwtUtils;
import com.poshtarenko.codeforge.security.pojo.JwtRefreshRequest;
import com.poshtarenko.codeforge.security.pojo.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;

    @Value("${jwt.refreshToken.expiration}")
    private int expiration;

    public String createToken(long userId) {
        var refreshToken = refreshTokenRepository.save(RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(new User(userId))
                .expiration(calcExpiration())
                .build());
        return refreshToken.getToken();
    }

    public JwtResponse refreshToken(JwtRefreshRequest refreshRequest) {
        var refreshToken = refreshTokenRepository
                .findRefreshTokenByToken(refreshRequest.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh Token %s not found!"
                        .formatted(refreshRequest.getRefreshToken())));

        if (isTokenExpired(refreshToken.getExpiration())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh Token %s was expired!"
                    .formatted(refreshRequest.getRefreshToken()));
        }

        String jwt = jwtUtils.generateJwt(refreshToken.getUser().getEmail());
        updateToken(refreshToken);
        return new JwtResponse(jwt, refreshToken.getToken());
    }

    private void updateToken(RefreshToken token) {
        token.setExpiration(calcExpiration());
        refreshTokenRepository.save(token);
    }

    private LocalDateTime calcExpiration() {
        return LocalDateTime.now().plus(expiration, ChronoUnit.MILLIS);
    }

    private boolean isTokenExpired(LocalDateTime expirationTime) {
        return expirationTime.isBefore(LocalDateTime.now());
    }

}
