package com.poshtarenko.codeforge.dto.request;

public record SaveProblemDTO(
        String name,
        String description,
        Long languageId,
        Long categoryId,
        String testingCode) {
}
