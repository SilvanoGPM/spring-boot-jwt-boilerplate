package com.skyg0d.spring.jwt.util.auth;

import com.skyg0d.spring.jwt.security.service.UserDetailsImpl;
import com.skyg0d.spring.jwt.util.user.UserCreator;

public class UserDetailsImplCreator {

    public static UserDetailsImpl createUserDetails() {
        return UserDetailsImpl.build(UserCreator.createUser());
    }

}
