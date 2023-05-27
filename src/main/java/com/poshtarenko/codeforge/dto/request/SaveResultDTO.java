package com.poshtarenko.codeforge.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record SaveResultDTO(
        Long testId,
        @JsonIgnore Long respondentId
) {
}
