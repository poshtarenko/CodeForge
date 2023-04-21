package com.poshtarenko.codeforge.dto;

public record SaveProblemDTO(
        String name,
        String description,
        Long languageId,
        Long categoryId,
        String testingCode) {
}
