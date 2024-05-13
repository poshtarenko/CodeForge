package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.AnswerMapper;
import com.poshtarenko.codeforge.dto.response.ViewAnswerDTO;
import com.poshtarenko.codeforge.entity.test.*;
import com.poshtarenko.codeforge.entity.user.Respondent;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.AnswerRepository;
import com.poshtarenko.codeforge.repository.RespondentRepository;
import com.poshtarenko.codeforge.repository.SolutionRepository;
import com.poshtarenko.codeforge.repository.TestRepository;
import com.poshtarenko.codeforge.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.poshtarenko.codeforge.entity.test.TaskCompletionStatus.NO_CODE;
import static com.poshtarenko.codeforge.entity.test.TaskCompletionStatus.TASK_COMPLETED;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('RESPONDENT')")
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final SolutionRepository solutionRepository;
    private final TestRepository testRepository;
    private final RespondentRepository respondentRepository;
    private final AnswerMapper answerMapper;

    @Override
    public ViewAnswerDTO find(long id) {
        return answerMapper.toDto(findById(id));
    }

    @Override
    @PreAuthorize("hasAuthority('AUTHOR')")
    public List<ViewAnswerDTO> findByTest(long testId) {
        return answerRepository.findAllByTestId(testId).stream()
                .map(answerMapper::toDto)
                .toList();
    }

    @Override
    public Optional<ViewAnswerDTO> findRespondentCurrentAnswer(long respondentId, long testId) {
        List<Answer> answers = answerRepository.findAllByRespondentIdAndTestIdOrderByCreatedAtDesc(respondentId, testId);
        if (answers.isEmpty()) {
            return Optional.empty();
        }
        ViewAnswerDTO answer = answerMapper.toDto(answers.get(0));
        return Optional.of(answer);
    }

    @Override
    @Transactional
    public ViewAnswerDTO startAnswer(long respondentId, String testCode) {
        Optional<Answer> answerOpt = answerRepository.findByRespondentIdAndTestInviteCode(respondentId, testCode);
        if (answerOpt.isPresent()) {
            return answerMapper.toDto(answerOpt.get());
        }
        Answer answer = new Answer();
        answer.setTest(testRepository.findByInviteCode(testCode)
                .orElseThrow(() -> new EntityNotFoundException(Test.class, "Test with code " + testCode + " not found")));
        answer.setRespondent(respondentRepository.findById(respondentId)
                .orElseThrow(() -> new EntityNotFoundException(Respondent.class, respondentId)));
        answer.setIsFinished(false);
        return answerMapper.toDto(answerRepository.save(answer));
    }

    @Override
    @Transactional
    public ViewAnswerDTO finishAnswer(long answerId) {
        Answer answer = findById(answerId);
        if (answer.getIsFinished().equals(true)) {
            throw new RuntimeException("Answer with id " + answerId + " already finished");
        }
        int score = answer.getSolutions().stream()
                .filter(s -> s.getTaskCompletionStatus().equals(TASK_COMPLETED))
                .map(Solution::getTask)
                .mapToInt(Task::getMaxScore)
                .sum();
        answer.setScore(score);
        answer.setIsFinished(true);
        fillAnswerWithEmptySolutions(answer);
        Answer saved = answerRepository.save(answer);
        return answerMapper.toDto(saved);
    }

    private void fillAnswerWithEmptySolutions(Answer answer){
        List<Long> solutionTasksIds = answer.getSolutions().stream().map(s -> s.getTask().getId()).toList();
        List<Task> uncompletedTasks = answer.getTest().getTasks().stream()
                .filter(task -> !solutionTasksIds.contains(task.getId()))
                .toList();
        List<Solution> emptySolutions = new ArrayList<>();
        for (Task uncompletedTask : uncompletedTasks) {
            Solution solution = new Solution();
            solution.setAnswer(answer);
            solution.setTask(uncompletedTask);
            solution.setTaskCompletionStatus(NO_CODE);
            emptySolutions.add(solution);
        }
        solutionRepository.saveAll(emptySolutions);
        List<Solution> solutions = new ArrayList<>();
        solutions.addAll(answer.getSolutions());
        solutions.addAll(emptySolutions);
        answer.setSolutions(solutions);
    }

    @Override
    @Transactional
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
                .orElseThrow(() -> new EntityNotFoundException(Answer.class, answerId));
    }
}