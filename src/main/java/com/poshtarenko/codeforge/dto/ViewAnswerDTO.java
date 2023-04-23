package com.poshtarenko.codeforge.dto;

public record ViewAnswerDTO(
        Long id,
        String code,
        Boolean isCompleted,
        Long taskId,
        Long respondentId
) {
}
