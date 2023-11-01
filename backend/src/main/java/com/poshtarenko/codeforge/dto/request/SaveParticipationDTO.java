package com.poshtarenko.codeforge.dto.request;

import jakarta.validation.constraints.Positive;

public record SaveParticipationDTO(
        @Positive Long lessonId,
        @Positive Long respondentId
) {
}
