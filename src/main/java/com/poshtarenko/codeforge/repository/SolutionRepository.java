package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
    @Query("select count(s) > 0 from Solution s where s.id = :answerId and s.answer.respondent.id = :respondentId")
    Boolean checkAccess(long answerId, long respondentId);

    @Query("select s from Solution s where s.answer.respondent.id = :respondentId and s.task.test.id = :testId")
    List<Solution> findByRespondentAndTest(long respondentId, long testId);

    Optional<Solution> findByTaskIdAndAnswerId(long respondentId, long answerId);
}
