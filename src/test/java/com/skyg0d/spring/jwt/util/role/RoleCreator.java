package com.skyg0d.spring.jwt.util.role;

import com.skyg0d.spring.jwt.model.ERole;
import com.skyg0d.spring.jwt.model.Role;

import java.util.UUID;

public class RoleCreator {

    public static final UUID uuid = UUID.fromString("357dc489-a26e-4959-ae07-33487d831193");

    public static Role createRoleToBeSave() {
        return Role
                .builder()
                .name(ERole.ROLE_USER)
                .build();
    }

    public static Role createRole() {
        return Role
                .builder()
                .id(uuid)
                .name(ERole.ROLE_USER)
                .build();
    }

}
