package com.poshtarenko.codeforge.dto.response;

public record ViewAnswerDTO(
        Long id,
        String code,
        Boolean isCompleted,
        Long taskId,
        Long respondentId
) {
}
