package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.Answer;
import com.poshtarenko.codeforge.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
}
