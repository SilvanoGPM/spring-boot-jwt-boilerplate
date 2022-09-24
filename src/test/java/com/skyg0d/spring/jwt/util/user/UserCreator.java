package com.skyg0d.spring.jwt.util.user;

import com.skyg0d.spring.jwt.model.ERole;
import com.skyg0d.spring.jwt.model.Role;
import com.skyg0d.spring.jwt.model.User;
import com.skyg0d.spring.jwt.payload.request.PromoteRequest;

import java.util.Set;
import java.util.UUID;

public class UserCreator {

    public static final UUID ID = UUID.fromString("eaf90e2e-ebe7-4c60-8a16-d7f4aa14b730");
    public static final String USERNAME = "SkyG0D";
    public static final String EMAIL = "test@mail.com";
    public static final String PASSWORD = "password";
    public static final Set<Role> ROLES = Set.of(new Role(ERole.ROLE_USER));

    public static User createUserToBeSave() {
        return User
                .builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .roles(ROLES)
                .build();
    }

    public static User createUser() {
        return User
                .builder()
                .id(ID)
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .roles(ROLES)
                .build();
    }

    public static PromoteRequest createPromoteRequest() {
        return PromoteRequest
                .builder()
                .userId(ID.toString())
                .roles(Set.of("admin"))
                .build();


    }
}
