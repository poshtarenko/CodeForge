package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.user.Respondent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespondentRepository extends JpaRepository<Respondent, Long> {

}
