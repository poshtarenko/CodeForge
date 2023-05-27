package com.poshtarenko.codeforge.dto.request;

public record UpdateProblemDTO(
        Long id,
        String name,
        String description,
        Long languageId,
        Long categoryId,
        String testingCode) {
}
