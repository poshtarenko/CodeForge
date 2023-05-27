package com.poshtarenko.codeforge.security.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    String token;
    String refreshToken;
    String role;
}
