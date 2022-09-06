package com.skyg0d.spring.jwt.repository;

import com.skyg0d.spring.jwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Boolean existsByEmail(String email);

}
