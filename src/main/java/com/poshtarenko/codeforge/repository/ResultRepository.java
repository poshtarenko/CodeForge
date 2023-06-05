package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.Answer;
import com.poshtarenko.codeforge.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    @Query("select count(r) > 0 from Result r where r.id = :resultId and r.respondent.id = :respondentId")
    Boolean checkAccess(long resultId, long respondentId);

    Optional<Result> findByRespondentIdAndTestId(long respondentId, long testId);

    List<Result> findByTestId(long testId);
}
