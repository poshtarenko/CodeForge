package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.ParticipationMapper;
import com.poshtarenko.codeforge.dto.request.CodeEvaluationRequest;
import com.poshtarenko.codeforge.dto.request.SaveParticipationDTO;
import com.poshtarenko.codeforge.dto.request.UpdateParticipationCodeDTO;
import com.poshtarenko.codeforge.dto.response.ViewParticipationDTO;
import com.poshtarenko.codeforge.entity.code.EvaluationResult;
import com.poshtarenko.codeforge.entity.lesson.Participation;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.ParticipationRepository;
import com.poshtarenko.codeforge.service.CodeEvaluationService;
import com.poshtarenko.codeforge.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {

    private final CodeEvaluationService codeEvaluationService;
    private final ParticipationRepository participationRepository;
    private final ParticipationMapper participationMapper;


    @Override
    public ViewParticipationDTO save(SaveParticipationDTO participationDTO) {
        Participation participation = participationMapper.toEntity(participationDTO);
        Participation savedParticipation = participationRepository.save(participation);
        return participationMapper.toDto(savedParticipation);
    }

    @Override
    public ViewParticipationDTO updateCode(UpdateParticipationCodeDTO participationDTO) {
        Participation participation = findById(participationDTO.id());
        participation.setCode(participationDTO.code());
        Participation savedParticipation = participationRepository.save(participation);
        return participationMapper.toDto(savedParticipation);
    }

    @Override
    public ViewParticipationDTO evaluateCode(Long participationId) {
        Participation participation = findById(participationId);

        CodeEvaluationRequest codeEvaluationRequest = new CodeEvaluationRequest(
                participation.getLesson().getLanguage().getName(),
                participation.getCode()
        );

        EvaluationResult evaluationResult = codeEvaluationService.evaluateCode(codeEvaluationRequest);
        participation.setEvaluationResult(evaluationResult);

        Participation savedParticipation = participationRepository.save(participation);
        return participationMapper.toDto(savedParticipation);
    }

    private Participation findById(long id) {
        return participationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        Participation.class, "Participation with id " + id + " not found")
                );
    }

}
