package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.entity.user.User;
import com.poshtarenko.codeforge.security.pojo.JwtResponse;
import com.poshtarenko.codeforge.security.pojo.SignInRequest;
import com.poshtarenko.codeforge.security.pojo.SignUpRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    User register(SignUpRequest signUpRequest);

    JwtResponse auth(SignInRequest signInRequest);

}
