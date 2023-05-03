package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.security.pojo.SignUpRequest;

public interface UserService {


    void register(SignUpRequest signUpRequest);

}
