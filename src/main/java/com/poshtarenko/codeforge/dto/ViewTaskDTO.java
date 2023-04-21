package com.poshtarenko.codeforge.dto;

public record ViewTaskDTO(
        Long id,
        String note,
        Integer maxScore,
        Long problemId,
        Long testId
        ) {
}
