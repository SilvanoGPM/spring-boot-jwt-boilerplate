package com.skyg0d.spring.jwt.service;

import com.skyg0d.spring.jwt.exception.TokenRefreshException;
import com.skyg0d.spring.jwt.exception.UserAlreadyExistsException;
import com.skyg0d.spring.jwt.model.ERole;
import com.skyg0d.spring.jwt.model.RefreshToken;
import com.skyg0d.spring.jwt.model.User;
import com.skyg0d.spring.jwt.payload.UserMachineDetails;
import com.skyg0d.spring.jwt.payload.request.SignupRequest;
import com.skyg0d.spring.jwt.payload.response.JwtResponse;
import com.skyg0d.spring.jwt.payload.response.TokenRefreshResponse;
import com.skyg0d.spring.jwt.repository.UserRepository;
import com.skyg0d.spring.jwt.security.jwt.JwtUtils;
import com.skyg0d.spring.jwt.security.service.UserDetailsImpl;
import com.skyg0d.spring.jwt.util.auth.AuthCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static com.skyg0d.spring.jwt.util.GenericCreator.createUserMachineDetails;
import static com.skyg0d.spring.jwt.util.auth.AuthCreator.*;
import static com.skyg0d.spring.jwt.util.auth.UserDetailsImplCreator.createUserDetails;
import static com.skyg0d.spring.jwt.util.role.RoleCreator.createRole;
import static com.skyg0d.spring.jwt.util.token.RefreshTokenCreator.createRefreshToken;
import static com.skyg0d.spring.jwt.util.user.UserCreator.createUser;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for AuthService")
public class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleService roleService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtUtils jwtUtils;

    @Mock
    RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        Authentication authenticationMock = Mockito.mock(Authentication.class);

        BDDMockito
                .when(authenticationMock.getPrincipal())
                .thenReturn(createUserDetails());

        BDDMockito
                .when(authenticationManager.authenticate(ArgumentMatchers.any(Authentication.class)))
                .thenReturn(authenticationMock);

        BDDMockito
                .when(jwtUtils.generateJwtToken(ArgumentMatchers.any(UserDetailsImpl.class)))
                .thenReturn(AuthCreator.TOKEN);

        BDDMockito
                .when(userRepository.save(ArgumentMatchers.any(User.class)))
                .thenReturn(createUser());

        BDDMockito
                .when(refreshTokenService.create(ArgumentMatchers.any(UUID.class), ArgumentMatchers.any(UserMachineDetails.class)))
                .thenReturn(createRefreshToken());

        BDDMockito
                .when(userRepository.existsByEmail(ArgumentMatchers.anyString()))
                .thenReturn(false);

        BDDMockito
                .when(roleService.findByName(ArgumentMatchers.any(ERole.class)))
                .thenReturn(createRole());

        BDDMockito
                .when(passwordEncoder.encode(ArgumentMatchers.any(CharSequence.class)))
                .thenReturn(PASSWORD);

        BDDMockito
                .when(refreshTokenService.findByToken(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(createRefreshToken()));

        BDDMockito
                .when(refreshTokenService.verifyExpiration(ArgumentMatchers.any(RefreshToken.class)))
                .thenReturn(createRefreshToken());

        BDDMockito
                .when(jwtUtils.generateTokenFromEmail(ArgumentMatchers.anyString()))
                .thenReturn(TOKEN);

        BDDMockito
                .doNothing()
                .when(refreshTokenService)
                .deleteByUserId(ArgumentMatchers.any(UUID.class));
    }

    @Test
    @DisplayName("signIn Authenticate And Returns Jwt Response When Successful")
    void signIn_AuthenticateAndReturnsJwtResponse_WhenSuccessful() {
        JwtResponse expectedResponse = createJwtResponse();

        JwtResponse jwtResponse = authService.signIn(createLoginRequest(), createUserMachineDetails());

        assertThat(jwtResponse.getToken()).isEqualTo(expectedResponse.getToken());
    }

    @Test
    @DisplayName("signUp Persists User When Successful")
    void signUp_PersistsUser_WhenSuccessful() {
        SignupRequest expectedUser = createSignupRequest();

        User userCreated = authService.signUp(expectedUser);

        assertThat(userCreated).isNotNull();

        assertThat(userCreated.getEmail()).isEqualTo(expectedUser.getEmail());
    }

    @Test
    @DisplayName("signUp Throws UserAlreadyExistsException When User Already Exists")
    void signUp_ThrowsUserAlreadyExistsException_WhenUserAlreadyExists() {
        BDDMockito
                .when(userRepository.existsByEmail(ArgumentMatchers.anyString()))
                .thenReturn(true);

        assertThatExceptionOfType(UserAlreadyExistsException.class)
                .isThrownBy(() -> authService.signUp(createSignupRequest()));
    }

    @Test
    @DisplayName("refreshToken Returns TokenRefreshResponse When Successful")
    void refreshToken_ReturnsTokenRefreshResponse_WhenSuccessful() {
        TokenRefreshResponse expectedResponse = createTokenRefreshResponse();

        TokenRefreshResponse tokenRefreshResponse = authService.refreshToken(createTokenRefreshRequest());

        assertThat(tokenRefreshResponse).isNotNull();

        assertThat(tokenRefreshResponse.getRefreshToken()).isEqualTo(expectedResponse.getRefreshToken());

        assertThat(tokenRefreshResponse.getAccessToken()).isEqualTo(expectedResponse.getAccessToken());
    }

    @Test
    @DisplayName("refreshToken Throws TokenRefreshException When Refresh Token Not Found")
    void refreshToken_ThrowsTokenRefreshException_WhenRefreshTokenNotFound() {
        BDDMockito
                .when(refreshTokenService.findByToken(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(TokenRefreshException.class)
                .isThrownBy(() -> authService.refreshToken(createTokenRefreshRequest()));
    }

    @Test
    @DisplayName("logout Removes Refresh Token When Successful")
    void logout_RemovesRefreshToken_WhenSuccessful() {
        assertThatCode(() -> authService.logout(UUID.randomUUID()))
                .doesNotThrowAnyException();
    }

}
