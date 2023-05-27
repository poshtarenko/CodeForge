package com.poshtarenko.codeforge.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record SaveTestDTO(
        String name,
        Integer maxDuration,
        @JsonIgnore Long authorId) {
}
