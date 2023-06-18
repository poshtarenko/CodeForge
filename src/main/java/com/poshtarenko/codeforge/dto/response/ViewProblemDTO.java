package com.poshtarenko.codeforge.dto.response;

public record ViewProblemDTO(
        Long id,
        String name,
        String description,
        ViewLanguageDTO language,
        ViewCategoryDTO category,
        String testingCode,
        String templateCode
) {
}
