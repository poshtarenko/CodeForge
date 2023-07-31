package com.poshtarenko.codeforge.dto.response;

public record ViewTaskDTO(
        Long id,
        String note,
        Integer maxScore,
        Long problemId,
        Long testId
) {
}
