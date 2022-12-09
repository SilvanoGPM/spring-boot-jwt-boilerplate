package com.skyg0d.spring.jwt.controller;

import com.skyg0d.spring.jwt.exception.BadRequestException;
import com.skyg0d.spring.jwt.model.User;
import com.skyg0d.spring.jwt.payload.UserMachineDetails;
import com.skyg0d.spring.jwt.payload.request.LoginRequest;
import com.skyg0d.spring.jwt.payload.request.SignupRequest;
import com.skyg0d.spring.jwt.payload.request.TokenRefreshRequest;
import com.skyg0d.spring.jwt.payload.response.JwtResponse;
import com.skyg0d.spring.jwt.payload.response.TokenRefreshResponse;
import com.skyg0d.spring.jwt.service.AuthService;
import com.skyg0d.spring.jwt.util.MockUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static com.skyg0d.spring.jwt.util.auth.AuthCreator.*;
import static com.skyg0d.spring.jwt.util.user.UserCreator.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for AuthController")
public class AuthControllerTest {

    @InjectMocks
    AuthController authController;

    @Mock
    AuthService authService;

    @BeforeEach
    void setUp() {
        BDDMockito
                .when(authService.signIn(ArgumentMatchers.any(LoginRequest.class), ArgumentMatchers.any(UserMachineDetails.class)))
                .thenReturn(createJwtResponse());

        BDDMockito
                .when(authService.signUp(ArgumentMatchers.any(SignupRequest.class)))
                .thenReturn(createUser());

        BDDMockito
                .when(authService.refreshToken(ArgumentMatchers.any(TokenRefreshRequest.class)))
                .thenReturn(createTokenRefreshResponse());

        BDDMockito
                .doNothing()
                .when(authService)
                .logout(ArgumentMatchers.any(UUID.class));
    }

    @Test
    @DisplayName("signIn Returns JwtResponse When Successful")
    void signIn_ReturnsJwtResponse_WhenSuccessful() {
        JwtResponse expectedResponse = createJwtResponse();

        HttpServletRequest httpServletRequest = MockUtils.mockUserMachineInfo();

        ResponseEntity<JwtResponse> entity = authController.signIn(createLoginRequest(), httpServletRequest);

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(entity.getBody()).isNotNull();

        assertThat(entity.getBody().getToken()).isEqualTo(expectedResponse.getToken());

        assertThat(entity.getBody().getRefreshToken()).isEqualTo(expectedResponse.getRefreshToken());
    }

    @Test
    @DisplayName("signUp_SaveUser_WhenSuccessful")
    void signUp_SaveUser_WhenSuccessful() {
        SignupRequest expectedUser = createSignupRequest();

        ResponseEntity<User> entity = authController.signUp(expectedUser);

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(entity.getBody()).isNotNull();

        assertThat(entity.getBody().getEmail()).isEqualTo(expectedUser.getEmail());
    }

    @Test
    @DisplayName("refreshToken Returns Token Refresh When Successful")
    void refreshToken_ReturnsTokenRefresh_WhenSuccessful() {
        TokenRefreshResponse expectedResponse = createTokenRefreshResponse();

        ResponseEntity<TokenRefreshResponse> entity = authController.refreshToken(createTokenRefreshRequest());

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(entity.getBody()).isNotNull();

        assertThat(entity.getBody().getAccessToken()).isEqualTo(expectedResponse.getAccessToken());

        assertThat(entity.getBody().getRefreshToken()).isEqualTo(expectedResponse.getRefreshToken());
    }

    @Test
    @DisplayName("logout Removes Refresh Token When Successful")
    void logout_RemovesRefreshToken_WhenSuccessful() {
        MockUtils.mockSecurityContextHolder();

        ResponseEntity<Void> entity = authController.logout();

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(entity.getBody()).isNull();
    }

    @Test
    @DisplayName("logout Throws BadRequestException When User Not Logged")
    void logout_ThrowsBadRequestException_WhenUserNotLogged() {
        MockUtils.mockSecurityContextHolder(true);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> authController.logout());
    }

}
