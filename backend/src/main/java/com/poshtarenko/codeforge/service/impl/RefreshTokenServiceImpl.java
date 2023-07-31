package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.entity.RefreshToken;
import com.poshtarenko.codeforge.entity.Role;
import com.poshtarenko.codeforge.entity.User;
import com.poshtarenko.codeforge.repository.RefreshTokenRepository;
import com.poshtarenko.codeforge.security.jwt.JwtUtils;
import com.poshtarenko.codeforge.security.pojo.JwtRefreshRequest;
import com.poshtarenko.codeforge.security.pojo.JwtResponse;
import com.poshtarenko.codeforge.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;

    @Value("${jwt.refreshToken.expiration}")
    private int expiration;

    @Override
    public String createToken(long userId) {
        var refreshToken = refreshTokenRepository.save(RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(new User(userId))
                .expiration(calcExpiration())
                .build());
        return refreshToken.getToken();
    }

    @Override
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

        updateToken(refreshToken);

        String jwt = jwtUtils.generateJwt(refreshToken.getUser().getEmail());
        String token = refreshToken.getToken();
        List<String> roles = refreshToken.getUser().getRoles().stream()
                .map(Role::getName)
                .map(ERole::getAuthority)
                .toList();
        return new JwtResponse(jwt, token, roles);
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
