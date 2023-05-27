package com.poshtarenko.codeforge.dto.model;

import java.util.Optional;

public record CodeEvaluationResult(
        boolean isCompleted,
        long evaluationTime,
        Optional<String> error
) {
}
