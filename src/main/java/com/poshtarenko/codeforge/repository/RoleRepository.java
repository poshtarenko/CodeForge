package com.poshtarenko.codeforge.repository;


import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
