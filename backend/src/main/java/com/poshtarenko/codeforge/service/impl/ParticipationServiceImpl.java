package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.ParticipationMapper;
import com.poshtarenko.codeforge.dto.request.CodeEvaluationRequest;
import com.poshtarenko.codeforge.dto.request.UpdateParticipationCodeDTO;
import com.poshtarenko.codeforge.dto.response.ViewParticipationDTO;
import com.poshtarenko.codeforge.entity.code.EvaluationResult;
import com.poshtarenko.codeforge.entity.lesson.Lesson;
import com.poshtarenko.codeforge.entity.lesson.Participation;
import com.poshtarenko.codeforge.entity.user.User;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.LessonRepository;
import com.poshtarenko.codeforge.repository.ParticipationRepository;
import com.poshtarenko.codeforge.repository.UserRepository;
import com.poshtarenko.codeforge.service.CodeEvaluationService;
import com.poshtarenko.codeforge.service.ParticipationService;
import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {

    private final CodeEvaluationService codeEvaluationService;
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final ParticipationMapper participationMapper;


    @Override
    public ViewParticipationDTO startParticipation(Long lessonId, Long userId) {
        Participation participation = new Participation();
        participation.setLesson(lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException(Lesson.class, lessonId)));
        participation.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class, userId)));
        Participation savedParticipation = participationRepository.save(participation);
        return participationMapper.toDto(savedParticipation);
    }

    @Override
    public ViewParticipationDTO updateCode(Long lessonId, UpdateParticipationCodeDTO participationDTO) {
        Participation participation = findById(lessonId);
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
                .orElseThrow(() -> new EntityNotFoundException(Participation.class, id));
    }

}
