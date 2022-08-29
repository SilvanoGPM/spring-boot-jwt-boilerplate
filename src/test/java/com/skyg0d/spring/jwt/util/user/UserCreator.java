package com.skyg0d.spring.jwt.util.user;

import com.skyg0d.spring.jwt.model.ERole;
import com.skyg0d.spring.jwt.model.Role;
import com.skyg0d.spring.jwt.model.User;

import java.util.Set;

public class UserCreator {

    public static User createUserToBeSave() {
        return User
                .builder()
                .id(1L)
                .username("SkyG0D")
                .email("test@mail.com")
                .password("password")
                .roles(Set.of(new Role(ERole.ROLE_USER)))
                .build();
    }

}
