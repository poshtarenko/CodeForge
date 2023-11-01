package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.lesson.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByAuthorId(long authorId);

    boolean existsLessonByInviteCode(String inviteCode);

}
