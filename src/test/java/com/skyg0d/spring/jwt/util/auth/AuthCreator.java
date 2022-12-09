package com.skyg0d.spring.jwt.util.auth;

import com.skyg0d.spring.jwt.model.User;
import com.skyg0d.spring.jwt.payload.request.LoginRequest;
import com.skyg0d.spring.jwt.payload.request.SignupRequest;
import com.skyg0d.spring.jwt.payload.request.TokenRefreshRequest;
import com.skyg0d.spring.jwt.payload.response.JwtResponse;
import com.skyg0d.spring.jwt.payload.response.TokenRefreshResponse;
import com.skyg0d.spring.jwt.util.token.RefreshTokenCreator;
import com.skyg0d.spring.jwt.util.user.UserCreator;

import java.util.List;

public class AuthCreator {

    public static final String USERNAME = "username";

    public static final String EMAIL = "test@mail.com";

    public static final String PASSWORD = "password";
    public static final String TOKEN = "token-test";

    public static final User USER = UserCreator.createUser();

    public static LoginRequest createLoginRequest() {
        return LoginRequest
                .builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();
    }

    public static SignupRequest createSignupRequest() {
        return SignupRequest
                .builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .build();
    }

    public static JwtResponse createJwtResponse() {
        return JwtResponse
                .builder()
                .token(TOKEN)
                .type("Bearer")
                .refreshToken(RefreshTokenCreator.TOKEN)
                .id(USER.getId().toString())
                .username(USER.getUsername())
                .email(USER.getEmail())
                .roles(List.of("ROLE_USER"))
                .build();
    }

    public static TokenRefreshRequest createTokenRefreshRequest() {
        return TokenRefreshRequest
                .builder()
                .refreshToken(RefreshTokenCreator.TOKEN)
                .build();
    }

    public static TokenRefreshResponse createTokenRefreshResponse() {
        return TokenRefreshResponse
                .builder()
                .accessToken(TOKEN)
                .refreshToken(RefreshTokenCreator.TOKEN)
                .tokenType("Bearer")
                .build();
    }

}
