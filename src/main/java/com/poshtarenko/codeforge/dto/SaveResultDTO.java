package com.poshtarenko.codeforge.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record SaveResultDTO(
        Long testId,
        @JsonIgnore Long respondentId
) {
}
