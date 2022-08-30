package com.skyg0d.spring.jwt.repository;

import com.skyg0d.spring.jwt.model.User;
import com.skyg0d.spring.jwt.util.user.UserCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.skyg0d.spring.jwt.util.user.UserCreator.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Tests for UserRepository")
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("findByUsername returns user when successful")
    void findByUsername_ReturnsUser_WhenSuccessful() {
        userRepository.save(createUserToBeSave());

        Optional<User> userFound = userRepository.findByUsername(createUserToBeSave().getUsername());

        assertThat(userFound).isNotEmpty();

        assertThat(userFound.get()).isNotNull();

        assertThat(userFound.get().getUsername()).isEqualTo(createUserToBeSave().getUsername());
    }

    @Test
    @DisplayName("existsByEmail returns true when email already exists")
    void existsByEmail_ReturnsTrue_WhenEmailAlreadyExists() {
        userRepository.save(createUserToBeSave());

        boolean userExists = userRepository.existsByEmail(createUserToBeSave().getEmail());

        assertThat(userExists).isTrue();
    }

    @Test
    @DisplayName("existsByEmail returns true when email don't exists")
    void existsByEmail_ReturnsFalse_WhenEmailDoNotExists() {
        boolean userExists = userRepository.existsByEmail(createUserToBeSave().getEmail());

        assertThat(userExists).isFalse();
    }

}
