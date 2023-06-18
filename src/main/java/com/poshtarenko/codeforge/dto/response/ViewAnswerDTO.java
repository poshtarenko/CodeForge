package com.poshtarenko.codeforge.dto.response;

import java.util.List;

public record ViewAnswerDTO(
        Long id,
        Integer score,
        Boolean isFinished,
        Long testId,
        RespondentDTO respondent,
        List<SolutionDTO> solutions
        ) {

    public record RespondentDTO(
            Long id,
            String name
    ) {
    }

    public record SolutionDTO(
            Long id,
            String code,
            Boolean isCompleted,
            TaskDTO task
    ) {
    }

    public record TaskDTO(
            Long id,
            String note,
            Integer maxScore,
            ProblemDTO problem
    ) {
    }

    public record ProblemDTO(
            Long id,
            String name,
            String description
    ) {
    }
}