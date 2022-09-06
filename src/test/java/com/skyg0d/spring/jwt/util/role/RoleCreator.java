package com.skyg0d.spring.jwt.util.role;

import com.skyg0d.spring.jwt.model.ERole;
import com.skyg0d.spring.jwt.model.Role;

public class RoleCreator {

    public static Role createRoleToBeSave() {
        return Role
                .builder()
                .id(1)
                .name(ERole.ROLE_USER)
                .build();
    }

}
