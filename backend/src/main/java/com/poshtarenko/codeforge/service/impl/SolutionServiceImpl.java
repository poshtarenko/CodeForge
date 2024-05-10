package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.SolutionMapper;
import com.poshtarenko.codeforge.dto.request.CodeEvaluationRequest;
import com.poshtarenko.codeforge.dto.request.CreateSolutionDTO;
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
import com.poshtarenko.codeforge.service.CodeTunerService;
import com.poshtarenko.codeforge.service.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.poshtarenko.codeforge.entity.test.TaskCompletionStatus.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('RESPONDENT')")
public class SolutionServiceImpl implements SolutionService {

    private static final String TESTING_CODE_TEMPLATE = """
            public class Main {
                public static void main(String[] args) {
                    Test test = new Test();
                    Solution solution = new Solution();
                    boolean isCompleted = test.test(solution);
                    if (isCompleted) {
                        System.out.println("SUCCESS");
                    } else {
                        System.out.println("FAILURE");
                    }
                }
            }
            """;

    private final CodeEvaluationService codeEvaluationService;
    private final CodeTunerService codeTunerService;
    private final SolutionRepository solutionRepository;
    private final ProblemRepository problemRepository;
    private final AnswerRepository answerRepository;
    private final SolutionMapper solutionMapper;

    @Override
    public ViewSolutionDTO find(long id) {
        return solutionRepository.findById(id)
                .map(solutionMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(Solution.class, id));
    }

    @Override
    @Transactional
    public ViewSolutionDTO put(CreateSolutionDTO solutionDTO) {
        Answer answer = answerRepository.findById(solutionDTO.answerId()).orElseThrow(
                () -> new EntityNotFoundException(Answer.class, solutionDTO.answerId()));
        if (answer.getIsFinished().equals(true)) {
            throw new RuntimeException("Trying to put solution to finished answer");
        }
        Solution solution = solutionMapper.toEntity(solutionDTO);
        solutionRepository.findByTaskIdAndAnswerId(solutionDTO.taskId(), solutionDTO.answerId())
                .ifPresent(s -> solution.setId(s.getId()));
        solution.setAnswer(answer);
        Problem problem = problemRepository.findByTask(solution.getTask().getId())
                .orElseThrow(() -> new EntityNotFoundException(Problem.class, "Task problem not found"));
        TaskCompletionStatus status = checkTaskCompletion(problem.getId(), solution.getCode());
        solution.setTaskCompletionStatus(status);
        return solutionMapper.toDto(solutionRepository.save(solution));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('AUTHOR', 'RESPONDENT')")
    public TaskCompletionStatus tryCode(TryCodeRequest tryCodeRequest) {
        return checkTaskCompletion(tryCodeRequest.problemId(), tryCodeRequest.code());
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

    private TaskCompletionStatus checkTaskCompletion(long problemId, String code) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new EntityNotFoundException(Problem.class, problemId));
        String language = problem.getLanguage().getName();
        String codeToEvaluate = "%s %s %s".formatted(TESTING_CODE_TEMPLATE, problem.getTestingCode(), code);
        codeToEvaluate = codeTunerService.tune(codeToEvaluate, language);

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
