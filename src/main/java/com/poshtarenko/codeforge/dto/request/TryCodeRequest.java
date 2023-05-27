package com.poshtarenko.codeforge.dto.request;

public record TryCodeRequest(
        String code,
        Long taskId) {
}
