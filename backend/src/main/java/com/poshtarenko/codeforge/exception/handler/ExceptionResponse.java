package com.poshtarenko.codeforge.exception.handler;

import java.time.LocalDateTime;

public record ExceptionResponse(
        String code,
        String message,
        LocalDateTime time
) {
}
