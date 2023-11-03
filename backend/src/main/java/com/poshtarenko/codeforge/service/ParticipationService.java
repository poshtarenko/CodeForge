package com.poshtarenko.codeforge.service;

import com.poshtarenko.codeforge.dto.request.SaveParticipationDTO;
import com.poshtarenko.codeforge.dto.request.UpdateParticipationCodeDTO;
import com.poshtarenko.codeforge.dto.response.ViewParticipationDTO;

public interface ParticipationService {

    ViewParticipationDTO save(SaveParticipationDTO participationDTO);

    ViewParticipationDTO updateCode(UpdateParticipationCodeDTO participationDTO);

    ViewParticipationDTO evaluateCode(Long participationId);

}
