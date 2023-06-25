package com.poshtarenko.codeforge.dto.response;

public record ViewSolutionDTO(
        Long id,
        String code,
        Boolean isCompleted,
        Long taskId,
        Long answerId
) {
}
