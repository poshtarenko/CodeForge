package com.poshtarenko.codeforge.exception;

import org.springframework.http.HttpStatus;

public class APIException extends RuntimeException {

    private final String apiErrorCode;
    private final HttpStatus httpStatus;

    public APIException(String message, String apiErrorCode, HttpStatus httpStatus) {
        super(message);
        this.apiErrorCode = apiErrorCode;
        this.httpStatus = httpStatus;
    }

    public String getApiErrorCode() {
        return apiErrorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
