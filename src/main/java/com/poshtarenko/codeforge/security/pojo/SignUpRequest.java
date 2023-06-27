package com.poshtarenko.codeforge.security.pojo;

import com.poshtarenko.codeforge.entity.ERole;


public record SignUpRequest(String email, String password, String username, ERole role) {
}
