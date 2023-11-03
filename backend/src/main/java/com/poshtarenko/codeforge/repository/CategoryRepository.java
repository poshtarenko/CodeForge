package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.test.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
