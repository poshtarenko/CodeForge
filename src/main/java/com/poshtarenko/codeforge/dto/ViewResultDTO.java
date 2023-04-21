package com.poshtarenko.codeforge.dto;

public record ViewResultDTO(
        Long id,
        Integer score,
        Long testId,
        Long respondentId) {
}
