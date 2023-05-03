package com.poshtarenko.codeforge.controller;

import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.security.jwt.JwtUtils;
import com.poshtarenko.codeforge.security.pojo.JwtRefreshRequest;
import com.poshtarenko.codeforge.security.pojo.JwtResponse;
import com.poshtarenko.codeforge.security.pojo.SignInRequest;
import com.poshtarenko.codeforge.security.pojo.SignUpRequest;
import com.poshtarenko.codeforge.security.userdetails.UserDetailsImpl;
import com.poshtarenko.codeforge.service.UserService;
import com.poshtarenko.codeforge.service.impl.RefreshTokenServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody SignInRequest signInRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        signInRequest.getEmail(),
                        signInRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwt(userDetails.getEmail());
        String refreshToken = refreshTokenService.createToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(
                        jwt,
                        refreshToken
                )
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignUpRequest signUpRequest) {
        if (signUpRequest.getRole().equals(ERole.ADMIN)) {
            throw new RuntimeException("Can not be registered as admin");
        }

        signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        userService.register(signUpRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshJwt(@RequestBody JwtRefreshRequest refreshRequest) {
        return ResponseEntity.ok(refreshTokenService.refreshToken(refreshRequest));
    }
}
