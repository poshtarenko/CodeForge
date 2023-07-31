package com.poshtarenko.codeforge.exception.handler;

import com.poshtarenko.codeforge.exception.APIException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(value = {APIException.class})
    public ResponseEntity<ExceptionResponse> apiException(APIException ex, WebRequest request) {

        ExceptionResponse response = new ExceptionResponse(
                ex.getApiErrorCode(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<ExceptionResponse>(response, ex.getHttpStatus());
    }

}
