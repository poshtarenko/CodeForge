package com.poshtarenko.codeforge.dto;

public record ViewProblemDTO(
        Long id,
        String name,
        String description,
        Long languageId,
        Long categoryId,
        String testingCode) {
}
