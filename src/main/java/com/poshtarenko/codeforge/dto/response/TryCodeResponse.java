package com.poshtarenko.codeforge.dto.response;

public record TryCodeResponse(
        boolean isCompleted,
        long evaluationTime,
        String error
) {
}
