package com.poshtarenko.codeforge.dto.response;

public record ViewSolutionDTO(
        Long id,
        String code,
        String taskCompletionStatus,
        Long taskId,
        Long answerId
) {
}
