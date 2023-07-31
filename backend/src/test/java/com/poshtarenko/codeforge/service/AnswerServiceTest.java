package com.poshtarenko.codeforge.service;

import com.poshtarenko.codeforge.dto.mapper.AnswerMapper;
import com.poshtarenko.codeforge.dto.mapper.AnswerMapperImpl;
import com.poshtarenko.codeforge.dto.mapper.EntityIdMapper;
import com.poshtarenko.codeforge.dto.response.ViewAnswerDTO;
import com.poshtarenko.codeforge.entity.Answer;
import com.poshtarenko.codeforge.entity.Respondent;
import com.poshtarenko.codeforge.entity.Solution;
import com.poshtarenko.codeforge.entity.Task;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.repository.AnswerRepository;
import com.poshtarenko.codeforge.repository.RespondentRepository;
import com.poshtarenko.codeforge.repository.TestRepository;
import com.poshtarenko.codeforge.service.impl.AnswerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test"})
class AnswerServiceTest {

    private static final long ANSWER_ID = 1L;
    private static final long TEST_ID = 1L;
    private static final String TEST_CODE = "qWErtY";
    private static final long RESPONDENT_ID = 1L;

    @Mock
    private AnswerRepository answerRepository;
    @Mock
    private TestRepository testRepository;
    @Mock
    private RespondentRepository respondentRepository;
    @Spy
    private AnswerMapper answerMapper = new AnswerMapperImpl(new EntityIdMapper());
    @InjectMocks
    private AnswerServiceImpl answerService;

    @Test
    void find() {
        Answer mockAnswer = new Answer(ANSWER_ID);
        mockAnswer.setTest(new com.poshtarenko.codeforge.entity.Test(TEST_ID));
        mockAnswer.setRespondent(new Respondent(RESPONDENT_ID));
        doReturn(Optional.of(mockAnswer))
                .when(answerRepository).findById(ANSWER_ID);

        ViewAnswerDTO actualResult = answerService.find(ANSWER_ID);

        assertAnswer(mockAnswer, actualResult);
    }

    @Test
    void findByTest() {
        Answer answer1 = new Answer(1L);
        answer1.setTest(new com.poshtarenko.codeforge.entity.Test(TEST_ID));
        answer1.setRespondent(new Respondent(1L));
        Answer answer2 = new Answer(2L);
        answer2.setTest(new com.poshtarenko.codeforge.entity.Test(TEST_ID));
        answer2.setRespondent(new Respondent(2L));
        List<Answer> answers = List.of(answer1, answer2);
        doReturn(answers)
                .when(answerRepository).findAllByTestId(TEST_ID);

        List<ViewAnswerDTO> actualResult = answerService.findByTest(TEST_ID);

        assertEquals(answers.size(), actualResult.size());
        assertAnswer(answers.get(0), actualResult.get(0));
        assertAnswer(answers.get(1), actualResult.get(1));
    }

    @Test
    void findRespondentCurrentAnswerWhenAnswersPresent() {
        Answer answerOld = new Answer(1L);
        answerOld.setTest(new com.poshtarenko.codeforge.entity.Test(TEST_ID));
        answerOld.setRespondent(new Respondent(RESPONDENT_ID));

        Answer answerCurrent = new Answer(2L);
        answerCurrent.setTest(new com.poshtarenko.codeforge.entity.Test(TEST_ID));
        answerCurrent.setRespondent(new Respondent(RESPONDENT_ID));
        List<Answer> answersOrderedByDate = List.of(answerCurrent, answerOld);

        doReturn(answersOrderedByDate)
                .when(answerRepository).findAllByRespondentIdAndTestIdOrderByCreatedAtDesc(RESPONDENT_ID, TEST_ID);

        Optional<ViewAnswerDTO> actualResult = answerService.findRespondentCurrentAnswer(RESPONDENT_ID, TEST_ID);

        assertTrue(actualResult.isPresent());
        assertAnswer(answerCurrent, actualResult.get());
    }

    @Test
    void findRespondentCurrentAnswerWhenNoAnswers() {
        doReturn(Collections.emptyList())
                .when(answerRepository).findAllByRespondentIdAndTestIdOrderByCreatedAtDesc(RESPONDENT_ID, TEST_ID);

        Optional<ViewAnswerDTO> actualResult = answerService.findRespondentCurrentAnswer(RESPONDENT_ID, TEST_ID);

        assertFalse(actualResult.isPresent());
    }

    @Test
    void startAnswer() {
        com.poshtarenko.codeforge.entity.Test test = new com.poshtarenko.codeforge.entity.Test(TEST_ID);
        Respondent respondent = new Respondent(RESPONDENT_ID);
        Answer answer = new Answer(ANSWER_ID);
        answer.setTest(test);
        answer.setRespondent(respondent);
        doReturn(answer)
                .when(answerRepository).save(any());
        doReturn(Optional.of(test))
                .when(testRepository).findByInviteCode(TEST_CODE);
        doReturn(Optional.of(respondent))
                .when(respondentRepository).findById(RESPONDENT_ID);

        ViewAnswerDTO actualResult = answerService.startAnswer(RESPONDENT_ID, TEST_CODE);

        assertAnswer(answer, actualResult);
    }

    @Test
    void finishAnswer() {
        Task task1 = new Task(1L);
        task1.setMaxScore(5);
        Solution rightSolution = new Solution(1L);
        rightSolution.setIsCompleted(true);
        rightSolution.setTask(task1);

        Task task2 = new Task(2L);
        task2.setMaxScore(3);
        Solution wrongSolution = new Solution(2L);
        wrongSolution.setIsCompleted(false);
        wrongSolution.setTask(task2);

        Answer initialAnswer = new Answer(ANSWER_ID);
        initialAnswer.setTest(new com.poshtarenko.codeforge.entity.Test(TEST_ID));
        initialAnswer.setRespondent(new Respondent(RESPONDENT_ID));
        initialAnswer.setSolutions(List.of(rightSolution, wrongSolution));
        initialAnswer.setIsFinished(false);

        Answer savedAnswer = new Answer(ANSWER_ID);
        savedAnswer.setTest(new com.poshtarenko.codeforge.entity.Test(TEST_ID));
        savedAnswer.setRespondent(new Respondent(RESPONDENT_ID));
        savedAnswer.setSolutions(List.of(rightSolution, wrongSolution));
        savedAnswer.setIsFinished(true);
        savedAnswer.setScore(5);

        doReturn(Optional.of(initialAnswer))
                .when(answerRepository).findById(ANSWER_ID);
        doReturn(savedAnswer)
                .when(answerRepository).save(initialAnswer);

        ViewAnswerDTO actualResult = answerService.finishAnswer(ANSWER_ID);

        assertAnswer(savedAnswer, actualResult);
    }

    @Test
    void delete() {
        doNothing().when(answerRepository).deleteById(ANSWER_ID);

        answerService.delete(ANSWER_ID);

        verify(answerRepository).deleteById(ANSWER_ID);
        verifyNoMoreInteractions(answerRepository);
    }

    @Test
    void checkAccessWhenNoAccess() {
        Answer answer = new Answer(ANSWER_ID);
        answer.setRespondent(new Respondent(5L));
        doReturn(Optional.of(answer))
                .when(answerRepository).findById(ANSWER_ID);

        assertThrows(EntityAccessDeniedException.class, () -> {
            answerService.checkAccess(ANSWER_ID, RESPONDENT_ID);
        });
    }

    @Test
    void checkAccessWhenAccessProvided() {
        Answer answer = new Answer(ANSWER_ID);
        answer.setRespondent(new Respondent(RESPONDENT_ID));
        doReturn(Optional.of(answer))
                .when(answerRepository).findById(ANSWER_ID);

        assertDoesNotThrow(() -> {
            answerService.checkAccess(ANSWER_ID, RESPONDENT_ID);
        });
    }

    private void assertAnswer(Answer expected, ViewAnswerDTO actual) {
        assertEquals(actual.id(), expected.getId());
        assertEquals(actual.testId(), expected.getTest().getId());
        assertEquals(actual.respondent().id(), expected.getRespondent().getId());
        assertEquals(actual.score(), expected.getScore());
        assertEquals(actual.isFinished(), expected.getIsFinished());
    }

}