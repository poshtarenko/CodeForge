package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.AnswerMapper;
import com.poshtarenko.codeforge.dto.response.ViewAnswerDTO;
import com.poshtarenko.codeforge.entity.*;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.*;
import com.poshtarenko.codeforge.service.AnswerService;
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
    private final SolutionRepository solutionRepository;
    private final TaskRepository taskRepository;
    private final AnswerMapper answerMapper;

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
                        Test.class, "Test with code " + testCode + " not found")
                ));
        answer.setRespondent(respondentRepository.findById(respondentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        Respondent.class, "Respondent with id " + respondentId + " not found")
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

    private int calculateScore(long answerId, long testId) {
        List<Solution> solutions = solutionRepository.findByRespondentAndTest(answerId, testId);
        List<Task> tasks = taskRepository.findByTestId(testId);

        int totalScore = 0;

        for (Task task : tasks) {
            Optional<Solution> maybeAnswer = solutions.stream()
                    .filter(s -> Objects.equals(task.getId(), s.getTask().getId()))
                    .findFirst();
            if (maybeAnswer.isPresent() && maybeAnswer.get().getIsCompleted()) {
                totalScore += task.getMaxScore();
            }
        }

        return totalScore;
    }

    private Answer findById(long answerId) {
        return answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        Answer.class, "Answer with id " + answerId + " not found")
                );
    }
}
