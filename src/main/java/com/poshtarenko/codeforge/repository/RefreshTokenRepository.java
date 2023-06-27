package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.RefreshToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @EntityGraph(attributePaths = {"user", "user.roles"})
    Optional<RefreshToken> findRefreshTokenByToken(String token);

}
