package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("select count(t) > 0 from Task t where t.id = :taskId and t.test.author.id = :authorId")
    Boolean checkAccess(long taskId, long authorId);

    List<Task> findByTestId(long testId);
}
