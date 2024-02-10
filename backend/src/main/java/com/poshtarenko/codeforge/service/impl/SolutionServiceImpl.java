package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.SolutionMapper;
import com.poshtarenko.codeforge.dto.request.CodeEvaluationRequest;
import com.poshtarenko.codeforge.dto.request.SaveSolutionDTO;
import com.poshtarenko.codeforge.dto.request.TryCodeRequest;
import com.poshtarenko.codeforge.dto.response.ViewSolutionDTO;
import com.poshtarenko.codeforge.entity.code.EvaluationResult;
import com.poshtarenko.codeforge.entity.test.*;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.AnswerRepository;
import com.poshtarenko.codeforge.repository.ProblemRepository;
import com.poshtarenko.codeforge.repository.SolutionRepository;
import com.poshtarenko.codeforge.service.CodeEvaluationService;
import com.poshtarenko.codeforge.service.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.poshtarenko.codeforge.entity.test.TaskCompletionStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('RESPONDENT')")
public class SolutionServiceImpl implements SolutionService {

    private final CodeEvaluationService codeEvaluationService;
    private final SolutionRepository solutionRepository;
    private final ProblemRepository problemRepository;
    private final AnswerRepository answerRepository;
    private final SolutionMapper solutionMapper;

    @Override
    public ViewSolutionDTO find(long id) {
        return solutionRepository.findById(id)
                .map(solutionMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        Solution.class, "Answer with id " + id + " not found")
                );
    }

    @Override
    @Transactional
    public ViewSolutionDTO put(SaveSolutionDTO solutionDTO) {
        Answer answer = answerRepository.findById(solutionDTO.answerId()).orElseThrow(
                () -> new EntityNotFoundException(Answer.class, "Answer with id " + solutionDTO.answerId() + " not found.")
        );
        if (answer.getIsFinished().equals(true)) {
            throw new RuntimeException("Trying to put solution to finished answer");
        }

        Solution solution = solutionMapper.toEntity(solutionDTO);
        solutionRepository.findByTaskIdAndAnswerId(solutionDTO.taskId(), solutionDTO.answerId())
                .ifPresent(s -> solution.setId(s.getId()));

        solution.setAnswer(answer);

        TaskCompletionStatus status = checkTaskCompletion(solution.getTask().getId(), solution.getCode());
        solution.setTaskCompletionStatus(status);

        Solution saved = solutionRepository.save(solution);
        return solutionMapper.toDto(saved);
    }

    @Override
    @Transactional
    public TaskCompletionStatus tryCode(TryCodeRequest tryCodeRequest) {
        return checkTaskCompletion(tryCodeRequest.taskId(), tryCodeRequest.code());
    }

    @Override
    @Transactional
    public void delete(long id) {
        solutionRepository.deleteById(id);
    }

    @Override
    public void checkAccess(long solutionId, long authorId) {
        if (solutionRepository.findById(solutionId).isEmpty()) {
            throw new EntityNotFoundException(Solution.class, "Answer with id %d not found".formatted(solutionId));
        }
        if (!solutionRepository.existsByIdAndAnswerRespondentId(solutionId, authorId)) {
            throw new EntityAccessDeniedException(Solution.class, solutionId, authorId);
        }
    }

    private TaskCompletionStatus checkTaskCompletion(long taskId, String code) {
        Problem problem = problemRepository.findByTask(taskId)
                .orElseThrow(() -> new EntityNotFoundException(
                        Problem.class, "Problem by task id " + taskId + " not found"));
        String language = problem.getLanguage().getName();
        String codeToEvaluate = problem.getTestingCode().formatted(code);

        CodeEvaluationRequest codeEvaluationRequest = new CodeEvaluationRequest(language, codeToEvaluate);
        EvaluationResult evaluationResult = codeEvaluationService.evaluateCode(codeEvaluationRequest);

        TaskCompletionStatus status;
        if (evaluationResult.getError() != null) {
            status = COMPILATION_ERROR;
        } else if (evaluationResult.getOutput() != null) {
            if (evaluationResult.getOutput().equals("SUCCESS")) {
                status = TASK_COMPLETED;
            } else {
                status = TASK_FAILED;
            }
        } else {
            throw new RuntimeException("Unknown task completion status");
        }

        return status;
    }
}
