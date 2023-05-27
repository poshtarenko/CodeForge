package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("select count(a) > 0 from Answer a where a.id = :answerId and a.respondent.id = :respondentId")
    Boolean checkAccess(long answerId, long respondentId);

    @Query("select a from Answer a where a.respondent.id = :respondentId and a.task.test.id = :testId")
    List<Answer> findByRespondentAndTest(long respondentId, long testId);

    Optional<Answer> findByTaskIdAndRespondentId(long respondentId, long taskId);
}
