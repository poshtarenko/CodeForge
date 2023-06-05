package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.request.SaveAnswerDTO;
import com.poshtarenko.codeforge.dto.request.TryCodeRequest;
import com.poshtarenko.codeforge.dto.request.UpdateAnswerDTO;
import com.poshtarenko.codeforge.dto.response.ViewAnswerDTO;
import com.poshtarenko.codeforge.dto.response.ViewProblemDTO;
import com.poshtarenko.codeforge.dto.mapper.AnswerMapper;
import com.poshtarenko.codeforge.entity.Answer;
import com.poshtarenko.codeforge.entity.Test;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.dto.request.CodeEvaluationRequest;
import com.poshtarenko.codeforge.dto.model.CodeEvaluationResult;
import com.poshtarenko.codeforge.repository.AnswerRepository;
import com.poshtarenko.codeforge.service.AnswerService;
import com.poshtarenko.codeforge.service.CodeEvaluationProvider;
import com.poshtarenko.codeforge.service.LanguageService;
import com.poshtarenko.codeforge.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;
    private final CodeEvaluationProvider codeEvaluationProvider;
    private final ProblemService problemService;
    private final LanguageService languageService;

    @Override
    public ViewAnswerDTO find(long id) {
        return answerRepository.findById(id)
                .map(answerMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        Answer.class, "Answer with id " + id + " not found")
                );
    }

    @Override
    public List<ViewAnswerDTO> findAnswersOnTest(long respondentId, long testId) {
        return answerRepository.findByRespondentAndTest(respondentId, testId).stream()
                .map(answerMapper::toDto)
                .toList();
    }

    @Override
    public ViewAnswerDTO put(SaveAnswerDTO answerDTO) {
        Optional<Answer> maybeAnswer = answerRepository
                .findByTaskIdAndRespondentId(answerDTO.respondentId(), answerDTO.taskId());

        Answer answerToSave = answerMapper.toEntity(answerDTO);;
        maybeAnswer.ifPresent(answer -> answerToSave.setId(answer.getId()));
        tryAnswer(answerToSave);

        return answerMapper.toDto(answerRepository.save(answerToSave));
    }

    @Override
    public CodeEvaluationResult tryCode(TryCodeRequest tryCodeRequest) {
        return tryCode(tryCodeRequest.taskId(), tryCodeRequest.code());
    }

    @Override
    public void delete(long id) {
        answerRepository.deleteById(id);
    }

    @Override
    public void checkAccess(long answerId, long authorId) {
        if (answerRepository.findById(answerId).isEmpty()) {
            throw new EntityNotFoundException(Answer.class, "Answer with id %d not found".formatted(answerId));
        }
        if (!answerRepository.checkAccess(answerId, authorId)) {
            throw new EntityAccessDeniedException(Answer.class, answerId, authorId);
        }
    }

    private void tryAnswer(Answer answer) {
        CodeEvaluationResult codeEvaluationResult = tryCode(answer.getTask().getId(), answer.getCode());
        answer.setIsCompleted(codeEvaluationResult.isCompleted());
        answer.setEvaluationTime(codeEvaluationResult.evaluationTime());
    }

    private CodeEvaluationResult tryCode(long taskId, String code) {
        ViewProblemDTO problem = problemService.findByTask(taskId);
        String language = languageService.find(problem.language().id()).name();
        String codeToEvaluate = problem.testingCode().formatted(code);

        CodeEvaluationRequest codeEvaluationRequest = new CodeEvaluationRequest(language, codeToEvaluate);
        return codeEvaluationProvider.evaluateCode(codeEvaluationRequest);
    }
}
