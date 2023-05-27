package com.poshtarenko.codeforge.dto.request;

public record SaveTaskDTO(
        String note,
        Integer maxScore,
        Long problemId,
        Long testId
) {
}
