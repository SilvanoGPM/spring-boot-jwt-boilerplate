package com.skyg0d.spring.jwt.util;

import com.skyg0d.spring.jwt.model.ERole;
import com.skyg0d.spring.jwt.model.Role;

import java.util.*;
import java.util.stream.Collectors;

public class RoleUtils {

    public static Map<String, ERole> roles = new HashMap<>() {{
        put("admin", ERole.ROLE_ADMIN);
        put("mod", ERole.ROLE_MODERATOR);
        put("user", ERole.ROLE_USER);
    }};

    public static ERole getRoleByString(String name) {
        return roles.getOrDefault(name.toLowerCase(), ERole.ROLE_USER);
    }



}
