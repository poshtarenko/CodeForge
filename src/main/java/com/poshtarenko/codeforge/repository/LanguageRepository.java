package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

}
