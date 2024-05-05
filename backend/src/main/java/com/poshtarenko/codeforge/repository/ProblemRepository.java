package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.test.Problem;
import com.poshtarenko.codeforge.service.impl.ProblemServiceImpl;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    @EntityGraph(attributePaths = {"language", "category"})
    @Query("select t.problem from Task t where t.id = :taskId")
    Optional<Problem> findByTask(long taskId);

    @EntityGraph(attributePaths = {"language", "category"})
    List<Problem> findByOwnerId(long ownerId);

    @EntityGraph(attributePaths = {"language", "category"})
    @Query("select p from Problem p where (p.owner.id = null) or (p.owner.id = :authorId and p.isCompleted)")
    List<Problem> findAllAvailableToAuthor(long authorId);
}
