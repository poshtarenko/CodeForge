package com.poshtarenko.codeforge.dto;

public record SaveAnswerDTO(
        String code,
        Long taskId,
        Long resultId,
        Long evaluationTime,
        Boolean isCompleted) {
}
