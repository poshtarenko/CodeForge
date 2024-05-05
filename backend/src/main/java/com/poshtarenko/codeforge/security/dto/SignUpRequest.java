package com.poshtarenko.codeforge.security.dto;

import com.poshtarenko.codeforge.entity.user.ERole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record SignUpRequest(@Email String email,
                            @NotBlank @Size(min = 8, max = 128) String password,
                            @Size(min = 5, max = 48) String username,
                            ERole role) {
}
