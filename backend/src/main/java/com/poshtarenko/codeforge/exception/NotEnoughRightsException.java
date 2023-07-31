package com.poshtarenko.codeforge.exception;

import com.poshtarenko.codeforge.entity.ERole;
import org.springframework.http.HttpStatus;

public class NotEnoughRightsException extends APIException {

    public static final String EXCEPTION_CODE = "003";
    public static final HttpStatus HTTP_STATUS = HttpStatus.UNAUTHORIZED;

    ERole requiredRole;

    public NotEnoughRightsException(String message, ERole requiredRole) {
        super(message, EXCEPTION_CODE, HTTP_STATUS);
        this.requiredRole = requiredRole;
    }
}
