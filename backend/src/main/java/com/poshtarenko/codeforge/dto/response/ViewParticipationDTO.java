package com.poshtarenko.codeforge.dto.response;

public record ViewParticipationDTO(
        Long id,
        Long lessonId,
        UserDTO user,
        String code,
        EvaluationResultDTO evaluationResult
) {
    public record UserDTO(
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
