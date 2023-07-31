package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.entity.User;
import com.poshtarenko.codeforge.security.pojo.SignUpRequest;

public interface UserService {

    User register(SignUpRequest signUpRequest);

}
