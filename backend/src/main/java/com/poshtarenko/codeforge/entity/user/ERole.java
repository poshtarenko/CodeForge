package com.poshtarenko.codeforge.entity.user;

import org.springframework.security.core.GrantedAuthority;

public enum ERole implements GrantedAuthority {
    AUTHOR, RESPONDENT;

    @Override
    public String getAuthority() {
        return name();
    }
}