package com.poshtarenko.codeforge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateProblemDTO(
        @NotBlank String name,
        @NotBlank String description,
        @Positive Long languageId,
        @NotBlank String templateCode,
        @NotBlank String testingCode) {
}
