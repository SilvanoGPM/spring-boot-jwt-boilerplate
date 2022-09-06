package com.skyg0d.spring.jwt.util.user;

import com.skyg0d.spring.jwt.model.ERole;
import com.skyg0d.spring.jwt.model.Role;
import com.skyg0d.spring.jwt.model.User;

import java.util.Set;
import java.util.UUID;

public class UserCreator {

    public static User createUserToBeSave() {
        return User
                .builder()
                .id(UUID.fromString("eaf90e2e-ebe7-4c60-8a16-d7f4aa14b730"))
                .username("SkyG0D")
                .email("test@mail.com")
                .password("password")
                .roles(Set.of(new Role(ERole.ROLE_USER)))
                .build();
    }

}
