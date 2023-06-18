package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.SolutionMapper;
import com.poshtarenko.codeforge.dto.model.CodeEvaluationResult;
import com.poshtarenko.codeforge.dto.request.CodeEvaluationRequest;
import com.poshtarenko.codeforge.dto.request.SaveSolutionDTO;
import com.poshtarenko.codeforge.dto.request.TryCodeRequest;
import com.poshtarenko.codeforge.dto.response.ViewProblemDTO;
import com.poshtarenko.codeforge.dto.response.ViewSolutionDTO;
import com.poshtarenko.codeforge.entity.Answer;
import com.poshtarenko.codeforge.entity.Solution;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.AnswerRepository;
import com.poshtarenko.codeforge.repository.SolutionRepository;
import com.poshtarenko.codeforge.service.CodeEvaluationProvider;
import com.poshtarenko.codeforge.service.LanguageService;
import com.poshtarenko.codeforge.service.ProblemService;
import com.poshtarenko.codeforge.service.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SolutionServiceImpl implements SolutionService {

    private final SolutionRepository solutionRepository;
    private final SolutionMapper solutionMapper;
    private final CodeEvaluationProvider codeEvaluationProvider;
    private final ProblemService problemService;
    private final AnswerRepository answerRepository;
    private final LanguageService languageService;

    @Override
    public ViewSolutionDTO find(long id) {
        return solutionRepository.findById(id)
                .map(solutionMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        Solution.class, "Answer with id " + id + " not found")
                );
    }

    @Override
    public List<ViewSolutionDTO> findAnswersOnTest(long respondentId, long testId) {
        return solutionRepository.findByRespondentAndTest(respondentId, testId).stream()
                .map(solutionMapper::toDto)
                .toList();
    }

    @Override
    public ViewSolutionDTO put(SaveSolutionDTO solution) {
        Optional<Solution> maybeSolution = solutionRepository
                .findByTaskIdAndAnswerId(solution.respondentId(), solution.answerId());
        Answer answer = answerRepository.findById(solution.answerId()).orElseThrow(
                () -> new EntityNotFoundException(Answer.class, "Answer with id " + solution.answerId() + " not found.")
        );
        if (answer.getIsFinished().equals(true)) {
            throw new RuntimeException("Trying to put solution to finished answer");
        }

        Solution solutionToSave = solutionMapper.toEntity(solution);
        solutionToSave.setAnswer(answer);
        maybeSolution.ifPresent(s -> solutionToSave.setId(s.getId()));
        tryAnswer(solutionToSave);
        return solutionMapper.toDto(solutionRepository.save(solutionToSave));
    }

    @Override
    public CodeEvaluationResult tryCode(TryCodeRequest tryCodeRequest) {
        return tryCode(tryCodeRequest.taskId(), tryCodeRequest.code());
    }

    @Override
    public void delete(long id) {
        solutionRepository.deleteById(id);
    }

    @Override
    public void checkAccess(long answerId, long authorId) {
        if (solutionRepository.findById(answerId).isEmpty()) {
            throw new EntityNotFoundException(Solution.class, "Answer with id %d not found".formatted(answerId));
        }
        if (!solutionRepository.checkAccess(answerId, authorId)) {
            throw new EntityAccessDeniedException(Solution.class, answerId, authorId);
        }
    }

    private void tryAnswer(Solution solution) {
        CodeEvaluationResult codeEvaluationResult = tryCode(solution.getTask().getId(), solution.getCode());
        solution.setIsCompleted(codeEvaluationResult.isCompleted());
        solution.setEvaluationTime(codeEvaluationResult.evaluationTime());
    }

    private CodeEvaluationResult tryCode(long taskId, String code) {
        ViewProblemDTO problem = problemService.findByTask(taskId);
        String language = languageService.find(problem.language().id()).name();
        String codeToEvaluate = problem.testingCode().formatted(code);

        CodeEvaluationRequest codeEvaluationRequest = new CodeEvaluationRequest(language, codeToEvaluate);
        return codeEvaluationProvider.evaluateCode(codeEvaluationRequest);
    }
}
