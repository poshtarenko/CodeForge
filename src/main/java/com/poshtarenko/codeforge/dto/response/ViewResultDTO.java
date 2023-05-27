package com.poshtarenko.codeforge.dto.response;

public record ViewResultDTO(
        Long id,
        Integer score,
        Long testId,
        Long respondentId) {
}
