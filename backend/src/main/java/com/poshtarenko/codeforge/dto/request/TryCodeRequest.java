package com.poshtarenko.codeforge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record TryCodeRequest(
        @NotBlank String code,
        @Positive Long problemId) {
}
