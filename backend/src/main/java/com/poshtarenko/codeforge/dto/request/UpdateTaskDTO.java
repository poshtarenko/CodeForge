package com.poshtarenko.codeforge.dto.request;

import jakarta.validation.constraints.Positive;

public record UpdateTaskDTO(
        @Positive Long id,
        String note,
        @Positive Integer maxScore,
        @Positive Long problemId,
        @Positive Long testId
) {
}
