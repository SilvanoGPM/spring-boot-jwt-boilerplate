package com.skyg0d.spring.jwt.util.token;

import com.skyg0d.spring.jwt.model.RefreshToken;
import com.skyg0d.spring.jwt.util.user.UserCreator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class RefreshTokenCreator {

    public static RefreshToken createRefreshTokenToBeSave() {
        return RefreshToken
                .builder()
                .id(UUID.fromString("b11b35d6-ad2c-4aa9-87b4-a58cea010f0a"))
                .token("token-test")
                .expiryDate(Instant.now().plus(1, ChronoUnit.HOURS))
                .user(UserCreator.createUserToBeSave())
                .build();
    }

}
