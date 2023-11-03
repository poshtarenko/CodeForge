package com.poshtarenko.codeforge.dto.response;

import java.util.List;

public record ViewLessonDTO(
        Long id,
        Long authorId,
        String name,
        String description,
        String inviteCode,
        ViewLanguageDTO language,
        List<ViewParticipationDTO> participations
) {
}
