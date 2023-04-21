package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.Author;
import com.poshtarenko.codeforge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

}
