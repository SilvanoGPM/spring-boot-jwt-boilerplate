package com.skyg0d.spring.jwt.repository;

import com.skyg0d.spring.jwt.model.RefreshToken;
import com.skyg0d.spring.jwt.model.Role;
import com.skyg0d.spring.jwt.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.skyg0d.spring.jwt.util.token.RefreshTokenCreator.createRefreshTokenToBeSave;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Tests for RefreshTokenRepository")
public class RefreshTokenRepositoryTest {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Test
    @DisplayName("findByToken Returns Refresh Token When Successful")
    void findByToken_ReturnsRefreshToken_WhenSuccessful() {
        User userToBeSave = createRefreshTokenToBeSave().getUser();

        List<Role> roles = roleRepository.saveAll(userToBeSave.getRoles());

        userToBeSave.setRoles(new HashSet<>(roles));

        User userSaved = userRepository.save(userToBeSave);

        RefreshToken tokenToBeSave = createRefreshTokenToBeSave();

        tokenToBeSave.setUser(userSaved);

        RefreshToken tokenSaved = refreshTokenRepository.save(tokenToBeSave);

        Optional<RefreshToken> tokenFound = refreshTokenRepository.findByToken(tokenSaved.getToken());

        assertThat(tokenFound).isNotEmpty();

        assertThat(tokenFound.get()).isNotNull();

        assertThat(tokenFound.get().getToken()).isEqualTo(createRefreshTokenToBeSave().getToken());
    }

    @Test
    @DisplayName("deleteByUser Removes Refresh Token When Successful")
    void deleteByUser_RemovesRefreshToken_WhenSuccessful() {
        User userToBeSave = createRefreshTokenToBeSave().getUser();

        List<Role> roles = roleRepository.saveAll(userToBeSave.getRoles());

        userToBeSave.setRoles(new HashSet<>(roles));

        User userSaved = userRepository.save(userToBeSave);

        RefreshToken tokenToBeSave = createRefreshTokenToBeSave();

        tokenToBeSave.setUser(userSaved);

        refreshTokenRepository.save(tokenToBeSave);

        refreshTokenRepository.deleteByUser(userSaved);

        long totalOfUsers = refreshTokenRepository.count();

        assertThat(totalOfUsers).isEqualTo(0);
    }

    @Test
    @DisplayName("findAllByUser Removes Refresh Token When Successful")
    void findAllByUser_ReturnsListOfRefreshTokenInsidePageObject_WhenSuccessful() {
        User userToBeSave = createRefreshTokenToBeSave().getUser();

        List<Role> roles = roleRepository.saveAll(userToBeSave.getRoles());

        userToBeSave.setRoles(new HashSet<>(roles));

        User userSaved = userRepository.save(userToBeSave);

        RefreshToken tokenToBeSave = createRefreshTokenToBeSave();

        tokenToBeSave.setUser(userSaved);

        RefreshToken tokenSaved = refreshTokenRepository.save(tokenToBeSave);

        Page<RefreshToken> usersPage = refreshTokenRepository
                .findAllByUser(PageRequest.of(0, 1), tokenSaved.getUser());

        assertThat(usersPage).isNotEmpty();

        assertThat(usersPage.getContent()).isNotEmpty();

        assertThat(usersPage.getContent().get(0)).isNotNull();

        assertThat(usersPage.getContent().get(0).getUser()).isEqualTo(tokenSaved.getUser());
    }

}
