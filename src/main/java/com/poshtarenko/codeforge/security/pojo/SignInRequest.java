package com.poshtarenko.codeforge.security.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SignInRequest {
    String email;
    String password;
}
