package com.poshtarenko.codeforge.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record FinishTestRequest(
        Long testId,
        @JsonIgnore Long respondentId
) {
}
