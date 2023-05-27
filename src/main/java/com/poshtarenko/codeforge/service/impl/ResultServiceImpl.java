package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.request.SaveResultDTO;
import com.poshtarenko.codeforge.dto.request.UpdateResultDTO;
import com.poshtarenko.codeforge.dto.response.ViewAnswerDTO;
import com.poshtarenko.codeforge.dto.response.ViewResultDTO;
import com.poshtarenko.codeforge.dto.response.ViewTaskDTO;
import com.poshtarenko.codeforge.dto.mapper.ResultMapper;
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
    public ViewResultDTO save(SaveResultDTO resultDTO) {
        Result result = resultMapper.toEntity(resultDTO);
        calculateScore(result);
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
        calculateScore(result);
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

    private void calculateScore(Result result) {
        List<ViewAnswerDTO> answers = answerService.findRespondentAnswersOnTest(
                result.getRespondent().getId(),
                result.getTest().getId()
        );
        List<ViewTaskDTO> tasks = taskService.findByTest(result.getTest().getId());

        int totalScore = 0;

        for (ViewTaskDTO task : tasks) {
            List<ViewAnswerDTO> answersOnTask = answers.stream().filter(a -> Objects.equals(a.taskId(), task.id())).toList();
            if (answersOnTask.size() != 1) {
                throw new RuntimeException("User was not answered on all tasks");
            }
            totalScore += task.maxScore();
        }

        result.setScore(totalScore);
    }
}
