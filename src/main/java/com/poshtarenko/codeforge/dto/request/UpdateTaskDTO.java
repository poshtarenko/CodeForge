package com.poshtarenko.codeforge.dto.request;

public record UpdateTaskDTO(
        Long id,
        String note,
        Integer maxScore,
        Long problemId,
        Long testId
) {
}
