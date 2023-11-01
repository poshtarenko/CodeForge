package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.test.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    boolean existsByIdAndTestAuthorId(long taskId, long authorId);

}
