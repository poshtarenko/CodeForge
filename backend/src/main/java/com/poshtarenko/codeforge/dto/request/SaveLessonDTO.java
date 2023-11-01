package com.poshtarenko.codeforge.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record SaveLessonDTO(
        @NotBlank String name,
        @Positive @JsonIgnore Long authorId
) {
}
