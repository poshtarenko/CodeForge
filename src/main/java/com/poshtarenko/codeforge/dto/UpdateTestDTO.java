package com.poshtarenko.codeforge.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record UpdateTestDTO(
        @JsonIgnore Long id,
        String name,
        Integer maxDuration,
        @JsonIgnore Long authorId) {
}
