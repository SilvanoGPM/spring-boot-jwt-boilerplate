package com.skyg0d.spring.jwt.service;

import com.skyg0d.spring.jwt.exception.ResourceNotFoundException;
import com.skyg0d.spring.jwt.model.ERole;
import com.skyg0d.spring.jwt.model.User;
import com.skyg0d.spring.jwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.skyg0d.spring.jwt.util.role.RoleCreator.createRole;
import static com.skyg0d.spring.jwt.util.user.UserCreator.createUser;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for UserService")
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleService roleService;

    @BeforeEach
    void setUp() {
        PageImpl<User> usersPage = new PageImpl<>(List.of(
                createUser()
        ));

        BDDMockito
                .when(userRepository.findAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(usersPage);

        BDDMockito
                .when(userRepository.findById(ArgumentMatchers.any(UUID.class)))
                .thenReturn(Optional.of(createUser()));

        BDDMockito
                .when(roleService.findByName(ArgumentMatchers.any(ERole.class)))
                .thenReturn(createRole());
    }

    @Test
    @DisplayName("listAll Returns List Of Users Inside Page Object When Successful")
    void listAll_ReturnsListOfUsersInsidePageObject_WhenSuccessful() {
        User expectedUser = createUser();

        Page<User> usersPage = userService.listAll(PageRequest.of(0, 1));

        assertThat(usersPage)
                .isNotEmpty()
                .contains(expectedUser);
    }

    @Test
    @DisplayName("findById Returns User When Successful")
    void findById_ReturnsUser_WhenSuccessful() {
        User expectedUser = createUser();

        User foundUser = userService.findById(UUID.randomUUID());

        assertThat(foundUser).isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("findById Throws ResourceNotFoundException When User Not Found")
    void findById_ThrowsResourceNotFoundException_WhenUserNotFound() {
        BDDMockito
                .when(userRepository.findById(ArgumentMatchers.any(UUID.class)))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> userService.findById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("promote Updates User Roles When Successful")
    void promote_UpdatesUserRoles_WhenSuccessful() {
        assertThatCode(() -> userService.promote(UUID.randomUUID(), Set.of("admin")))
                .doesNotThrowAnyException();

        assertThatCode(() -> userService.promote(UUID.randomUUID(), Set.of()))
                .doesNotThrowAnyException();
    }

}
