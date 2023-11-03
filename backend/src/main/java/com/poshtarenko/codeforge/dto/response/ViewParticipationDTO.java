package com.poshtarenko.codeforge.dto.response;

public record ViewParticipationDTO(
        Long id,
        Long lessonId,
        RespondentDTO respondent,
        String code,
        EvaluationResultDTO evaluationResult
) {
    public record RespondentDTO(
            Long id,
            String username
    ) {
    }

    public record EvaluationResultDTO(
            String output,
            String error
    ) {
    }
}
