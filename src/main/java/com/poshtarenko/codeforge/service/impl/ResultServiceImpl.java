package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.ResultMapper;
import com.poshtarenko.codeforge.dto.request.FinishTestRequest;
import com.poshtarenko.codeforge.dto.request.UpdateResultDTO;
import com.poshtarenko.codeforge.dto.response.ViewAnswerDTO;
import com.poshtarenko.codeforge.dto.response.ViewResultDTO;
import com.poshtarenko.codeforge.dto.response.ViewTaskDTO;
import com.poshtarenko.codeforge.entity.Respondent;
import com.poshtarenko.codeforge.entity.Result;
import com.poshtarenko.codeforge.entity.Test;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.ResultRepository;
import com.poshtarenko.codeforge.service.AnswerService;
import com.poshtarenko.codeforge.service.ResultService;
import com.poshtarenko.codeforge.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;
    private final ResultMapper resultMapper;
    private final AnswerService answerService;
    private final TaskService taskService;

    @Override
    public ViewResultDTO find(long id) {
        return resultRepository.findById(id)
                .map(resultMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        Result.class, "Result with id " + id + " not found")
                );
    }

    @Override
    public Optional<ViewResultDTO> findRespondentTestResult(long respondentId, long testId) {
        return resultRepository.findByRespondentIdAndTestId(respondentId, testId)
                .map(resultMapper::toDto);
    }

    @Override
    public List<ViewResultDTO> findTestResults(long testId) {
        return resultRepository.findByTestId(testId).stream()
                .map(resultMapper::toDto)
                .toList();
    }

    @Override
    public ViewResultDTO save(FinishTestRequest finishTestRequest) {
        Result result = new Result();
        result.setRespondent(new Respondent(finishTestRequest.respondentId()));
        result.setTest(new Test(finishTestRequest.testId()));
        int score = calculateScore(result.getRespondent().getId(), result.getTest().getId());
        result.setScore(score);
        return resultMapper.toDto(resultRepository.save(result));
    }

    @Override
    public void delete(long id) {
        resultRepository.deleteById(id);
    }

    @Override
    public ViewResultDTO update(UpdateResultDTO resultDTO) {
        Result result = resultRepository.findById(resultDTO.id())
                .orElseThrow(() -> new EntityNotFoundException(
                        Test.class,
                        "Test with id %d not found".formatted(resultDTO.id())));
        int score = calculateScore(result.getRespondent().getId(), result.getTest().getId());
        result.setScore(score);
        return resultMapper.toDto(resultRepository.save(result));
    }

    @Override
    public void checkAccess(long resultId, long authorId) {
        if (resultRepository.findById(resultId).isEmpty()) {
            throw new EntityNotFoundException(Result.class, "Result with id %d not found".formatted(resultId));
        }
        if (!resultRepository.checkAccess(resultId, authorId)) {
            throw new EntityAccessDeniedException(Result.class, resultId, authorId);
        }
    }

    private int calculateScore(long respondentId, long testId) {
        List<ViewAnswerDTO> answers = answerService.findAnswersOnTest(respondentId, testId);
        List<ViewTaskDTO> tasks = taskService.findByTest(testId);

        int totalScore = 0;

        for (ViewTaskDTO task : tasks) {
            Optional<ViewAnswerDTO> maybeAnswer = answers.stream()
                    .filter(a -> Objects.equals(a.taskId(), task.id()))
                    .findFirst();
            if (maybeAnswer.isPresent() && maybeAnswer.get().isCompleted()) {
                totalScore += task.maxScore();
            }
        }

        return totalScore;
    }
}
