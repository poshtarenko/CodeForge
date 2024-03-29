package com.poshtarenko.codeforge.service;

import com.poshtarenko.codeforge.dto.request.UpdateParticipationCodeDTO;
import com.poshtarenko.codeforge.dto.response.ViewParticipationDTO;

public interface ParticipationService {

    ViewParticipationDTO startParticipation(Long lessonId, Long userId);

    ViewParticipationDTO updateCode(Long lessonId, UpdateParticipationCodeDTO participationDTO);

    ViewParticipationDTO evaluateCode(Long participationId);

}
