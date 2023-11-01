package com.poshtarenko.codeforge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateParticipationCodeDTO(
        @Positive Long id,
        @NotBlank String code
) {
}
