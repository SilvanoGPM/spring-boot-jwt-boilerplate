package com.skyg0d.spring.jwt.service;

import com.skyg0d.spring.jwt.exception.TokenRefreshException;
import com.skyg0d.spring.jwt.model.RefreshToken;
import com.skyg0d.spring.jwt.model.User;
import com.skyg0d.spring.jwt.payload.response.UserTokenResponse;
import com.skyg0d.spring.jwt.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.skyg0d.spring.jwt.util.GenericCreator.createUserMachineDetails;
import static com.skyg0d.spring.jwt.util.token.RefreshTokenCreator.createRefreshToken;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for RefreshTokenService")
public class RefreshTokenServiceTest {

    @InjectMocks
    RefreshTokenService refreshTokenService;

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @Mock
    UserService userService;

    @BeforeEach
    void setUp() {
        PageImpl<RefreshToken> tokensPage = new PageImpl<>(List.of(
                createRefreshToken()
        ));

        BDDMockito
                .when(refreshTokenRepository.findAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(tokensPage);

        BDDMockito
                .when(userService.findById(ArgumentMatchers.any(UUID.class)))
                .thenReturn(createRefreshToken().getUser());

        BDDMockito
                .when(refreshTokenRepository.findAllByUser(ArgumentMatchers.any(Pageable.class), ArgumentMatchers.any(User.class)))
                .thenReturn(tokensPage);

        BDDMockito
                .when(refreshTokenRepository.findByToken(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(createRefreshToken()));

        BDDMockito
                .when(refreshTokenRepository.save(ArgumentMatchers.any(RefreshToken.class)))
                .thenReturn(createRefreshToken());

        BDDMockito
                .doNothing()
                .when(refreshTokenRepository)
                .deleteByUser(ArgumentMatchers.any(User.class));
    }

    @Test
    @DisplayName("listAll Returns List Of Refresh Token Inside Page Object When Successful")
    void listAll_ReturnsListOfRefreshTokenInsidePageObject_WhenSuccessful() {
        RefreshToken expectedToken = createRefreshToken();

        Page<RefreshToken> tokensPage = refreshTokenService.listAll(PageRequest.of(0, 1));

        assertThat(tokensPage)
                .isNotEmpty()
                .hasSize(1)
                .contains(expectedToken);

    }

    @Test
    @DisplayName("listAllByUser Returns List Of User Token Inside Page Object When Successful")
    void listAllByUser_ReturnsListOfUserTokenInsidePageObject_WhenSuccessful() {
        RefreshToken expectedToken = createRefreshToken();

        Page<UserTokenResponse> tokensPage = refreshTokenService.listAllByUser(PageRequest.of(0, 1), UUID.randomUUID());

        assertThat(tokensPage)
                .isNotEmpty()
                .hasSize(1);

        assertThat(tokensPage.getContent().get(0)).isNotNull();

        assertThat(tokensPage.getContent().get(0).getId()).isEqualTo(expectedToken.getId().toString());
    }

    @Test
    @DisplayName("findByToken Returns Refresh Token Inside Optional Object When Successful")
    void findByToken_ReturnsRefreshTokenInsideOptionalObject_WhenSuccessful() {
        RefreshToken expectedToken = createRefreshToken();

        Optional<RefreshToken> tokenOptional = refreshTokenService.findByToken("test");

        assertThat(tokenOptional).isNotEmpty();

        assertThat(tokenOptional.get()).isEqualTo(expectedToken);
    }

    @Test
    @DisplayName("create Persists Refresh Token When Successful")
    void create_PersistsRefreshToken_WhenSuccessful() {
        RefreshToken expectedToken = createRefreshToken();

        RefreshToken savedRefreshToken = refreshTokenService.create(UUID.randomUUID(), createUserMachineDetails());

        assertThat(savedRefreshToken).isEqualTo(expectedToken);
    }

    @Test
    @DisplayName("verifyExpiration Checks Token Expiration And Returns Token When Successful")
    void verifyExpiration_ChecksTokenExpirationAndReturnsToken_WhenSuccessful() {
        RefreshToken expectedToken = createRefreshToken();

        RefreshToken checkedToken = refreshTokenService.verifyExpiration(createRefreshToken());

        assertThat(checkedToken).isEqualTo(expectedToken);
    }

    @Test
    @DisplayName("verifyExpiration Throws TokenRefreshException When Token Expired")
    void verifyExpiration_ThrowsTokenRefreshException_WhenTokenExpired() {
        RefreshToken refreshToken = createRefreshToken();

        refreshToken.setExpiryDate(Instant.now().minusMillis(10));

        assertThatExceptionOfType(TokenRefreshException.class)
                .isThrownBy(() -> refreshTokenService.verifyExpiration(refreshToken));
    }

    @Test
    @DisplayName("deleteByUser Removes Token When Successful")
    void deleteByUser_RemovesToken_WhenSuccessful() {
        assertThatCode(() -> refreshTokenService.deleteByUserId(createRefreshToken().getUser().getId()))
                .doesNotThrowAnyException();
    }

}
