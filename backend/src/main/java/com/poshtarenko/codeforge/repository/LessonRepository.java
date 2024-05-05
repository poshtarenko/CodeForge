package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.lesson.Lesson;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @EntityGraph(attributePaths = "participations")
    Optional<Lesson> findById(Long id);

    List<Lesson> findByAuthorId(long authorId);

    boolean existsLessonByInviteCode(String inviteCode);

    Optional<Lesson> findByInviteCode(String inviteCode);
}
