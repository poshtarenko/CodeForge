package com.poshtarenko.codeforge.service;

import com.poshtarenko.codeforge.dto.response.ViewAnswerDTO;
import com.poshtarenko.codeforge.entity.Answer;
import com.poshtarenko.codeforge.entity.Respondent;
import com.poshtarenko.codeforge.entity.Solution;
import com.poshtarenko.codeforge.entity.Task;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles({"test"})
class AnswerServiceTest {

    private static final long ANSWER_ID = 1L;
    private static final long TEST_ID = 1L;
    private static final String TEST_CODE = "qWErtY";
    private static final long RESPONDENT_ID = 1L;

    @MockBean
    private AnswerRepository answerRepository;
    @MockBean
    private TestRepository testRepository;
    @MockBean
    private RespondentRepository respondentRepository;
    @MockBean
    private SolutionRepository solutionRepository;
    @MockBean
    private TaskRepository taskRepository;
    @Autowired
    private AnswerService answerService;

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
                .when(answerRepository).findByTestId(TEST_ID);

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
        answerOld.setCreatedAt(LocalDateTime.of(2021, 10, 10, 10, 10, 10));

        Answer answerCurrent = new Answer(2L);
        answerCurrent.setTest(new com.poshtarenko.codeforge.entity.Test(TEST_ID));
        answerCurrent.setRespondent(new Respondent(RESPONDENT_ID));
        answerCurrent.setCreatedAt(LocalDateTime.of(2022, 10, 10, 10, 10, 10));
        List<Answer> answersOrderedByDate = List.of(answerCurrent, answerOld);

        doReturn(answersOrderedByDate)
                .when(answerRepository).findByRespondentIdAndTestIdOrderByCreatedAtDesc(RESPONDENT_ID, TEST_ID);

        Optional<ViewAnswerDTO> actualResult = answerService.findRespondentCurrentAnswer(RESPONDENT_ID, TEST_ID);

        assertTrue(actualResult.isPresent());
        assertAnswer(answerCurrent, actualResult.get());
    }

    @Test
    void findRespondentCurrentAnswerWhenNoAnswers() {
        doReturn(Collections.emptyList())
                .when(answerRepository).findByRespondentIdAndTestIdOrderByCreatedAtDesc(RESPONDENT_ID, TEST_ID);

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
        wrongSolution.setIsCompleted(true);
        wrongSolution.setTask(task2);

        Answer answerOnFind = new Answer(ANSWER_ID);
        answerOnFind.setTest(new com.poshtarenko.codeforge.entity.Test(TEST_ID));
        answerOnFind.setRespondent(new Respondent(RESPONDENT_ID));
        answerOnFind.setSolutions(List.of(rightSolution, wrongSolution));
        answerOnFind.setIsFinished(false);

        Answer answerAfterSave = new Answer(ANSWER_ID);
        answerAfterSave.setTest(new com.poshtarenko.codeforge.entity.Test(TEST_ID));
        answerAfterSave.setRespondent(new Respondent(RESPONDENT_ID));
        answerAfterSave.setSolutions(List.of(rightSolution, wrongSolution));
        answerAfterSave.setIsFinished(true);
        answerAfterSave.setScore(5);

        doReturn(Optional.of(answerOnFind))
                .when(answerRepository).findById(ANSWER_ID);
        doReturn(answerAfterSave)
                .when(answerRepository).save(any());
        doReturn(List.of(rightSolution, wrongSolution))
                .when(solutionRepository).findByRespondentAndTest(RESPONDENT_ID, TEST_ID);
        doReturn(List.of(task1, task2))
                .when(taskRepository).findByTestId(TEST_ID);

        ViewAnswerDTO actualResult = answerService.finishAnswer(ANSWER_ID);

        assertAnswer(answerAfterSave, actualResult);
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
        doReturn(Optional.of(new Answer(1L)))
                .when(answerRepository).findById(ANSWER_ID);
        doReturn(false)
                .when(answerRepository).checkAccess(ANSWER_ID, RESPONDENT_ID);

        assertThrows(EntityAccessDeniedException.class, () -> {
            answerService.checkAccess(ANSWER_ID, RESPONDENT_ID);
        });
    }

    @Test
    void checkAccessWhenAccessProvided() {
        doReturn(Optional.of(new Answer(1L)))
                .when(answerRepository).findById(ANSWER_ID);
        doReturn(true)
                .when(answerRepository).checkAccess(ANSWER_ID, RESPONDENT_ID);

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