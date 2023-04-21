package com.poshtarenko.codeforge.security.pojo;

import com.poshtarenko.codeforge.entity.ERole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SignUpRequest {
    String email;
    String password;
    String username;
    ERole role;
}
