package com.poshtarenko.codeforge.security.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    String token;
    String refreshToken;
    List<String> roles;
}
