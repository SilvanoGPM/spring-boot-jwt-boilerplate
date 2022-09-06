package com.skyg0d.spring.jwt.util.role;

import com.skyg0d.spring.jwt.model.ERole;
import com.skyg0d.spring.jwt.model.Role;

import java.util.UUID;

public class RoleCreator {

    public static Role createRoleToBeSave() {
        return Role
                .builder()
                .id(UUID.fromString("357dc489-a26e-4959-ae07-33487d831193"))
                .name(ERole.ROLE_USER)
                .build();
    }

}
