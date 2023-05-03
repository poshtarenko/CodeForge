package com.poshtarenko.codeforge.dto;

public record SaveTaskDTO(
        String note,
        Integer maxScore,
        Long problemId,
        Long testId
) {
}
