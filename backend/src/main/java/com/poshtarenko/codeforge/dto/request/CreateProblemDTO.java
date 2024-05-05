package com.poshtarenko.codeforge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateProblemDTO(
        @NotBlank String name,
        @Positive Long languageId
) {
}
