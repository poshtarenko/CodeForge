package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.AnswerMapper;
import com.poshtarenko.codeforge.dto.response.ViewAnswerDTO;
import com.poshtarenko.codeforge.dto.response.ViewSolutionDTO;
import com.poshtarenko.codeforge.dto.response.ViewTaskDTO;
import com.poshtarenko.codeforge.entity.Answer;
import com.poshtarenko.codeforge.entity.Respondent;
import com.poshtarenko.codeforge.entity.Test;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.AnswerRepository;
import com.poshtarenko.codeforge.repository.RespondentRepository;
import com.poshtarenko.codeforge.repository.TestRepository;
import com.poshtarenko.codeforge.service.AnswerService;
import com.poshtarenko.codeforge.service.SolutionService;
import com.poshtarenko.codeforge.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final TestRepository testRepository;
    private final RespondentRepository respondentRepository;
    private final AnswerMapper answerMapper;
    private final SolutionService solutionService;
    private final TaskService taskService;

    @Override
    public ViewAnswerDTO find(long id) {
        return answerMapper.toDto(findById(id));
    }

    @Override
    public List<ViewAnswerDTO> findByTest(long testId) {
        return answerRepository.findByTestId(testId).stream()
                .map(answerMapper::toDto)
                .toList();
    }

    @Override
    public Optional<ViewAnswerDTO> findRespondentCurrentAnswer(long respondentId, long testId) {
        List<Answer> answers = answerRepository.findByRespondentIdAndTestIdOrderByCreatedAtDesc(respondentId, testId);
        if (answers.size() == 0) {
            return Optional.empty();
        }
        ViewAnswerDTO answer = answerMapper.toDto(answers.get(0));
        return Optional.of(answer);
    }

    @Override
    public ViewAnswerDTO startAnswer(long respondentId, String testCode) {
        Answer answer = new Answer();
        answer.setTest(testRepository.findByInviteCode(testCode)
                .orElseThrow(() -> new EntityNotFoundException(
                        Test.class, "Test with code " + testCode + " not found;")
                ));
        answer.setRespondent(respondentRepository.findById(respondentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        Respondent.class, "Respondent with id " + respondentId + " not found;")
                ));
        answer.setIsFinished(false);
        answer.setCreatedAt(LocalDateTime.now());
        return answerMapper.toDto(answerRepository.save(answer));
    }

    @Override
    public ViewAnswerDTO finishAnswer(long answerId) {
        Answer answer = findById(answerId);

        if (answer.getIsFinished().equals(true)) {
            throw new RuntimeException("Answer with id " + answerId + " already finished");
        }
        int score = calculateScore(answer.getRespondent().getId(), answer.getTest().getId());
        answer.setScore(score);
        answer.setIsFinished(true);

        Answer saved = answerRepository.save(answer);
        return answerMapper.toDto(saved);
    }

    @Override
    public void delete(long id) {
        answerRepository.deleteById(id);
    }

    @Override
    public void checkAccess(long resultId, long authorId) {
        if (answerRepository.findById(resultId).isEmpty()) {
            throw new EntityNotFoundException(Answer.class, "Result with id %d not found".formatted(resultId));
        }
        if (!answerRepository.checkAccess(resultId, authorId)) {
            throw new EntityAccessDeniedException(Answer.class, resultId, authorId);
        }
    }

    private int calculateScore(long respondentId, long testId) {
        List<ViewSolutionDTO> solutions = solutionService.findAnswersOnTest(respondentId, testId);
        List<ViewTaskDTO> tasks = taskService.findByTest(testId);

        int totalScore = 0;

        for (ViewTaskDTO task : tasks) {
            Optional<ViewSolutionDTO> maybeAnswer = solutions.stream()
                    .filter(a -> Objects.equals(a.taskId(), task.id()))
                    .findFirst();
            if (maybeAnswer.isPresent() && maybeAnswer.get().isCompleted()) {
                totalScore += task.maxScore();
            }
        }

        return totalScore;
    }

    private Answer findById(long answerId) {
        return answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        Answer.class, "Result with id " + answerId + " not found")
                );
    }
}
