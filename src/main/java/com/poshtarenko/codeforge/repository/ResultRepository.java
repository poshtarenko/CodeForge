package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    @Query("select count(r) > 0 from Result r where r.id = :resultId and r.respondent.id = :respondentId")
    Boolean checkAccess(long resultId, long respondentId);
}
