package com.skyg0d.spring.jwt.repository;

import com.skyg0d.spring.jwt.model.Role;
import com.skyg0d.spring.jwt.util.role.RoleCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.skyg0d.spring.jwt.util.role.RoleCreator.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Tests for RefreshTokenRepository")
public class RoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;

    @Test
    @DisplayName("findByName Returns Role When Successful")
    void findByName_ReturnsRole_WhenSuccessful() {
        roleRepository.save(createRoleToBeSave());

        Optional<Role> roleFound = roleRepository.findByName(createRoleToBeSave().getName());

        assertThat(roleFound).isNotEmpty();

        assertThat(roleFound.get().getName()).isEqualTo(createRoleToBeSave().getName());
    }

}
