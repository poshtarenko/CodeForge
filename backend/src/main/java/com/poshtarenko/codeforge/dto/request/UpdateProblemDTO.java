package com.poshtarenko.codeforge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateProblemDTO(
        @NotBlank String name,
        String description,
        @Positive Long languageId,
        String templateCode,
        String testingCode) {
}
