package com.poshtarenko.codeforge.entity;

import org.springframework.security.core.GrantedAuthority;

public enum ERole implements GrantedAuthority {
    AUTHOR, RESPONDENT, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}