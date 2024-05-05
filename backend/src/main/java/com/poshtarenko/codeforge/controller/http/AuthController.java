package com.poshtarenko.codeforge.controller.http;

import com.poshtarenko.codeforge.security.dto.JwtRefreshRequest;
import com.poshtarenko.codeforge.security.dto.JwtResponse;
import com.poshtarenko.codeforge.security.dto.SignInRequest;
import com.poshtarenko.codeforge.security.dto.SignUpRequest;
import com.poshtarenko.codeforge.service.RefreshTokenService;
import com.poshtarenko.codeforge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public JwtResponse login(@RequestBody @Validated SignInRequest signInRequest) {
        return userService.auth(signInRequest);
    }

    @PostMapping("/register")
    public JwtResponse register(@RequestBody @Validated SignUpRequest signUpRequest) {
        userService.register(signUpRequest);
        return userService.auth(new SignInRequest(
                signUpRequest.email(),
                signUpRequest.password()
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshJwt(@RequestBody @Validated JwtRefreshRequest refreshRequest) {
        return ResponseEntity.ok(refreshTokenService.refreshToken(refreshRequest));
    }
}
