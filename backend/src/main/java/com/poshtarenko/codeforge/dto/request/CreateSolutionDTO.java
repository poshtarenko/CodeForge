package com.poshtarenko.codeforge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateSolutionDTO(
        @NotBlank String code,
        @Positive Long taskId,
        @Positive Long answerId) {
}
