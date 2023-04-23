package com.poshtarenko.codeforge.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record UpdateAnswerDTO(
        Long id,
        String code) {
}
