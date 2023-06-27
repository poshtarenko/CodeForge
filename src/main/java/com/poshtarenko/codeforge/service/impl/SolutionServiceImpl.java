package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.SolutionMapper;
import com.poshtarenko.codeforge.dto.model.CodeEvaluationResult;
import com.poshtarenko.codeforge.dto.request.CodeEvaluationRequest;
import com.poshtarenko.codeforge.dto.request.SaveSolutionDTO;
import com.poshtarenko.codeforge.dto.request.TryCodeRequest;
import com.poshtarenko.codeforge.dto.response.ViewSolutionDTO;
import com.poshtarenko.codeforge.entity.Answer;
import com.poshtarenko.codeforge.entity.Problem;
import com.poshtarenko.codeforge.entity.Solution;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.AnswerRepository;
import com.poshtarenko.codeforge.repository.ProblemRepository;
import com.poshtarenko.codeforge.repository.SolutionRepository;
import com.poshtarenko.codeforge.service.CodeEvaluationProvider;
import com.poshtarenko.codeforge.service.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SolutionServiceImpl implements SolutionService {

    private final CodeEvaluationProvider codeEvaluationProvider;
    private final SolutionRepository solutionRepository;
    private final ProblemRepository problemRepository;
    private final AnswerRepository answerRepository;
    private final SolutionMapper solutionMapper;

    @Override
    @Transactional(readOnly = true)
    public ViewSolutionDTO find(long id) {
        return solutionRepository.findById(id)
                .map(solutionMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        Solution.class, "Answer with id " + id + " not found")
                );
    }

    @Override
    public ViewSolutionDTO put(SaveSolutionDTO solution) {
        Optional<Solution> maybeSolution = solutionRepository
                .findByTaskIdAndAnswerId(solution.taskId(), solution.answerId());
        Answer answer = answerRepository.findById(solution.answerId()).orElseThrow(
                () -> new EntityNotFoundException(Answer.class, "Answer with id " + solution.answerId() + " not found.")
        );
        if (answer.getIsFinished().equals(true)) {
            throw new RuntimeException("Trying to put solution to finished answer");
        }

        Solution solutionToSave = solutionMapper.toEntity(solution);
        maybeSolution.ifPresent(s -> solutionToSave.setId(s.getId()));
        solutionToSave.setAnswer(answer);
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
    @Transactional(readOnly = true)
    public void checkAccess(long solutionId, long authorId) {
        if (solutionRepository.findById(solutionId).isEmpty()) {
            throw new EntityNotFoundException(Solution.class, "Answer with id %d not found".formatted(solutionId));
        }
        if (!solutionRepository.existsByIdAndAnswerRespondentId(solutionId, authorId)) {
            throw new EntityAccessDeniedException(Solution.class, solutionId, authorId);
        }
    }

    private void tryAnswer(Solution solution) {
        CodeEvaluationResult codeEvaluationResult = tryCode(solution.getTask().getId(), solution.getCode());
        solution.setIsCompleted(codeEvaluationResult.isCompleted());
        solution.setEvaluationTime(codeEvaluationResult.evaluationTime());
    }

    private CodeEvaluationResult tryCode(long taskId, String code) {
        Problem problem = problemRepository.findByTask(taskId)
                .orElseThrow(() -> new EntityNotFoundException(
                        Problem.class, "Problem by task id " + taskId + " not found")
                );
        String language = problem.getLanguage().getName();
        String codeToEvaluate = problem.getTestingCode().formatted(code);

        CodeEvaluationRequest codeEvaluationRequest = new CodeEvaluationRequest(language, codeToEvaluate);
        return codeEvaluationProvider.evaluateCode(codeEvaluationRequest);
    }
}
