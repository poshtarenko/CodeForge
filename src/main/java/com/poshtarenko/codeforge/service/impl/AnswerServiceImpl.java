package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.AnswerMapper;
import com.poshtarenko.codeforge.dto.response.ViewAnswerDTO;
import com.poshtarenko.codeforge.entity.*;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.AnswerRepository;
import com.poshtarenko.codeforge.repository.RespondentRepository;
import com.poshtarenko.codeforge.repository.TestRepository;
import com.poshtarenko.codeforge.service.AnswerService;
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
    private final TestRepository testRepository;
    private final RespondentRepository respondentRepository;
    private final AnswerMapper answerMapper;

    @Override
    @Transactional(readOnly = true)
    public ViewAnswerDTO find(long id) {
        return answerMapper.toDto(findById(id));
    }

    @Override
    public List<ViewAnswerDTO> findByTest(long testId) {
        return answerRepository.findAllByTestId(testId).stream()
                .map(answerMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ViewAnswerDTO> findRespondentCurrentAnswer(long respondentId, long testId) {
        List<Answer> answers = answerRepository.findAllByRespondentIdAndTestIdOrderByCreatedAtDesc(respondentId, testId);
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
        return answerMapper.toDto(answerRepository.save(answer));
    }

    @Override
    public ViewAnswerDTO finishAnswer(long answerId) {
        Answer answer = findById(answerId);

        if (answer.getIsFinished().equals(true)) {
            throw new RuntimeException("Answer with id " + answerId + " already finished");
        }
        int score = answer.getSolutions().stream()
                .filter(Solution::getIsCompleted)
                .map(Solution::getTask)
                .mapToInt(Task::getMaxScore)
                .sum();
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
    public void checkAccess(long answerId, long respondentId) {
        Answer answer = findById(answerId);
        if (!answer.getRespondent().getId().equals(respondentId)) {
            throw new EntityAccessDeniedException(Answer.class, answerId, respondentId);
        }
    }

    private Answer findById(long answerId) {
        return answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        Answer.class, "Answer with id " + answerId + " not found")
                );
    }
}
