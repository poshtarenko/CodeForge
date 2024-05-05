package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.lesson.Participation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    Optional<Participation> findByUserIdAndLessonId(Long userId, Long lessonId);
}
