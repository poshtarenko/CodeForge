package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.lesson.Lesson;
import com.poshtarenko.codeforge.entity.test.Answer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByAuthorId(long authorId);

    boolean existsLessonByInviteCode(String inviteCode);

}
