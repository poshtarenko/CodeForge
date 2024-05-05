package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.entity.user.User;
import com.poshtarenko.codeforge.security.dto.JwtResponse;
import com.poshtarenko.codeforge.security.dto.SignInRequest;
import com.poshtarenko.codeforge.security.dto.SignUpRequest;

public interface UserService {

    User register(SignUpRequest signUpRequest);

    JwtResponse auth(SignInRequest signInRequest);

}
