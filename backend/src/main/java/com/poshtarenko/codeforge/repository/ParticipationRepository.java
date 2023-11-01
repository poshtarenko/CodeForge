package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.lesson.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {

}
