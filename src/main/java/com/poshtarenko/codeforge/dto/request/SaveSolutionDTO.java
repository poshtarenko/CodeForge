package com.poshtarenko.codeforge.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record SaveSolutionDTO(
        String code,
        Long taskId,
        Long answerId,
        @JsonIgnore Long respondentId) {
}
