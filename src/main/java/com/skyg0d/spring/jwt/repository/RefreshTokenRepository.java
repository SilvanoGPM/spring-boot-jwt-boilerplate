package com.skyg0d.spring.jwt.repository;

import com.skyg0d.spring.jwt.model.RefreshToken;
import com.skyg0d.spring.jwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    void deleteByUser(User user);
}
