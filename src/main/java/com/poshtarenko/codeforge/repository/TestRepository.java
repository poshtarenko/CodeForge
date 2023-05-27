package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

    List<Test> findAllByAuthorId(Long authorId);

    Optional<Test> findByCode(String code);

    @Query("select count(t) > 0 from Test t where t.id = :testId and t.author.id = :authorId")
    Boolean checkAccess(long testId, long authorId);

}
