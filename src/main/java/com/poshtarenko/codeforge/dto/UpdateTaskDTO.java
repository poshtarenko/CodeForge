package com.poshtarenko.codeforge.dto;

public record UpdateTaskDTO(
        Long id,
        String note,
        Integer maxScore,
        Long problemId,
        Long testId
        ) {
}
