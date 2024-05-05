package com.poshtarenko.codeforge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateTestDTO(
        @NotBlank String name,
        @Positive Integer maxDuration) {
}
