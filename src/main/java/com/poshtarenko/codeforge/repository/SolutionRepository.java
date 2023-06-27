package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.Solution;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {

    boolean existsByIdAndAnswerRespondentId(long solutionId, long respondentId);

    @EntityGraph(attributePaths = {"task"})
    Optional<Solution> findByTaskIdAndAnswerId(long taskId, long answerId);
}
