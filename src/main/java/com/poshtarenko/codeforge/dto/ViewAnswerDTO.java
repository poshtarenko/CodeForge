package com.poshtarenko.codeforge.dto;

public record ViewAnswerDTO(
        Long id,
        String code,
        Long taskId,
        Long resultId,
        Long evaluationTime,
        Boolean isCompleted) {
}
