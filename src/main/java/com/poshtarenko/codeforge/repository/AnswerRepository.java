package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("select count(a) > 0 from Answer a where a.id = :resultId and a.respondent.id = :respondentId")
    Boolean checkAccess(long resultId, long respondentId);

    List<Answer> findByRespondentIdAndTestIdOrderByCreatedAtDesc(long respondentId, long testId);

    List<Answer> findByRespondentIdAndTestId(long respondentId, long testId);

    List<Answer> findByTestId(long testId);
}
