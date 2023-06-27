package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    boolean existsByIdAndTestAuthorId(long taskId, long authorId);

    List<Task> findAllByTestId(long testId);
}
