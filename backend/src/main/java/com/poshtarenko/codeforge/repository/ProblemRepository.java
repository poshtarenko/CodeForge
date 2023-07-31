package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.Problem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

//    @EntityGraph(attributePaths = {"language", "category"})
//    List<Problem> findAll();

    @EntityGraph(attributePaths = {"language", "category"})
    @Query("select t.problem from Task t where t.id = :taskId")
    Optional<Problem> findByTask(long taskId);

}
