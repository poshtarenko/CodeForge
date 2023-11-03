package com.poshtarenko.codeforge.dto.response;

public record ViewSolutionDTO(
        Long id,
        String code,
        ViewSolutionResultDTO evaluationResult,
        Long taskId,
        Long answerId
) {
}
