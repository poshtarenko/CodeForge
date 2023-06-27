package com.poshtarenko.codeforge.controller;

import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.security.jwt.JwtUtils;
import com.poshtarenko.codeforge.security.pojo.JwtRefreshRequest;
import com.poshtarenko.codeforge.security.pojo.JwtResponse;
import com.poshtarenko.codeforge.security.pojo.SignInRequest;
import com.poshtarenko.codeforge.security.pojo.SignUpRequest;
import com.poshtarenko.codeforge.security.userdetails.UserDetailsImpl;
import com.poshtarenko.codeforge.service.RefreshTokenService;
import com.poshtarenko.codeforge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody SignInRequest signInRequest) {
        return auth(signInRequest.email(), signInRequest.password());
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@RequestBody SignUpRequest signUpRequest) {
        if (signUpRequest.role().equals(ERole.ADMIN)) {
            throw new RuntimeException("Can not be registered as admin");
        }

        userService.register(signUpRequest);
        return auth(signUpRequest.email(), signUpRequest.password());
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshJwt(@RequestBody JwtRefreshRequest refreshRequest) {
        return ResponseEntity.ok(refreshTokenService.refreshToken(refreshRequest));
    }

    private ResponseEntity<JwtResponse> auth(String email, String password) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwt(userDetails.getEmail());
        String refreshToken = refreshTokenService.createToken(userDetails.getId());
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User without role"));

        return ResponseEntity.ok(new JwtResponse(
                        jwt,
                        refreshToken,
                        role
                )
        );
    }
}
