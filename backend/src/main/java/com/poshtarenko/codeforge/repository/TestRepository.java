package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.test.Test;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

    @EntityGraph(attributePaths = {"tasks", "tasks.problem", "tasks.problem.language", "tasks.problem.category"})
    Optional<Test> findById(long id);

    @EntityGraph(attributePaths = {"tasks", "tasks.problem", "tasks.problem.language", "tasks.problem.category"})
    List<Test> findAllByAuthorId(Long authorId);

    Optional<Test> findByInviteCode(String inviteCode);

}
