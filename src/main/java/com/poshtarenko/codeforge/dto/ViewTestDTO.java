package com.poshtarenko.codeforge.dto;

import com.poshtarenko.codeforge.entity.Problem;

import java.util.List;

public record ViewTestDTO(
        Long id,
        String name,
        Integer maxDuration,
        Long authorId,
        List<TaskDTO> tasks
) {
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
            String description,
            String language,
            String category
    ) {
    }
}
