package com.poshtarenko.codeforge.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record SaveSolutionDTO(
        @NotBlank String code,
        @Positive Long taskId,
        @Positive Long answerId,
        @JsonIgnore Long respondentId) {
}
