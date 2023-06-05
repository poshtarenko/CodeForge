package com.poshtarenko.codeforge.dto.response;

public record ViewResultDTO(
        Long id,
        Integer score,
        Long testId,
        RespondentDTO respondent) {

    public record RespondentDTO(
            Long id,
            String name
    ) {
    }
}