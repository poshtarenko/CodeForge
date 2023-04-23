package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.Answer;
import com.poshtarenko.codeforge.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("select count(a) > 0 from Answer a where a.id = :answerId and a.respondent.id = :respondentId")
    Boolean checkAccess(long answerId, long respondentId);
}
