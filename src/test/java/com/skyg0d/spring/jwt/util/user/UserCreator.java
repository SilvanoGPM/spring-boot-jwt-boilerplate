package com.skyg0d.spring.jwt.util.user;

import com.skyg0d.spring.jwt.model.ERole;
import com.skyg0d.spring.jwt.model.Role;
import com.skyg0d.spring.jwt.model.User;

import java.util.Set;
import java.util.UUID;

public class UserCreator {

    public static final UUID uuid = UUID.fromString("eaf90e2e-ebe7-4c60-8a16-d7f4aa14b730");
    public static final String username = "SkyG0D";
    public static final String email = "test@mail.com";
    public static final String password = "password";
    public static final Set<Role> roles = Set.of(new Role(ERole.ROLE_USER));

    public static User createUserToBeSave() {
        return User
                .builder()
                .username(username)
                .email(email)
                .password(password)
                .roles(roles)
                .build();
    }

    public static User createUser() {
        return User
                .builder()
                .id(uuid)
                .username(username)
                .email(email)
                .password(password)
                .roles(roles)
                .build();
    }

}
