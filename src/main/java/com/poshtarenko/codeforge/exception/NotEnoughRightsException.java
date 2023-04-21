package com.poshtarenko.codeforge.exception;

import com.poshtarenko.codeforge.entity.BaseEntity;
import com.poshtarenko.codeforge.entity.ERole;

public class NotEnoughRightsException extends RuntimeException {

    ERole requiredRole;

    public NotEnoughRightsException(String message, ERole requiredRole) {
        super(message);
        this.requiredRole = requiredRole;
    }
}
