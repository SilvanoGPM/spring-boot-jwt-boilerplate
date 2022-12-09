package com.skyg0d.spring.jwt.integration;

import com.skyg0d.spring.jwt.model.RefreshToken;
import com.skyg0d.spring.jwt.model.User;
import com.skyg0d.spring.jwt.payload.request.PromoteRequest;
import com.skyg0d.spring.jwt.payload.response.MessageResponse;
import com.skyg0d.spring.jwt.payload.response.UserTokenResponse;
import com.skyg0d.spring.jwt.repository.RefreshTokenRepository;
import com.skyg0d.spring.jwt.repository.UserRepository;
import com.skyg0d.spring.jwt.util.JWTCreator;
import com.skyg0d.spring.jwt.wrapper.PageableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Set;

import static com.skyg0d.spring.jwt.util.token.RefreshTokenCreator.createRefreshToken;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Integration tests for UserController")
public class UserControllerIT {

    @Autowired
    TestRestTemplate httpClient;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JWTCreator jwtCreator;

    @Test
    @DisplayName("listAll Returns List Of Users Inside Page Object When Successful")
    void listAll_ReturnsListOfUsersInsidePageObject_WhenSuccessful() {
        ResponseEntity<PageableResponse<User>> entity = httpClient.exchange(
                "/users",
                HttpMethod.GET,
                jwtCreator.createAdminAuthEntity(null),
                new ParameterizedTypeReference<>() {
                });

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(entity.getBody()).isNotEmpty();
    }

    @Test
    @DisplayName("listAllTokens Returns List Of Refresh Tokens Inside Page Object When Successful")
    void listAllTokens_ReturnsListOfRefreshTokensInsidePageObject_WhenSuccessful() {
        RefreshToken refreshToken = createRefreshToken();

        refreshToken.setUser(findUserByEmail("user@mail.com"));

        RefreshToken expectedRefreshToken = refreshTokenRepository.save(refreshToken);

        ResponseEntity<PageableResponse<RefreshToken>> entity = httpClient.exchange(
                "/users/tokens",
                HttpMethod.GET,
                jwtCreator.createAdminAuthEntity(null),
                new ParameterizedTypeReference<>() {
                });

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(entity.getBody())
                .isNotEmpty()
                .contains(expectedRefreshToken);
    }

    @Test
    @DisplayName("listMyAllTokens Returns List Of Refresh Tokens Inside Page Object When Successful")
    void listMyAllTokens_ReturnsListOfRefreshTokensInsidePageObject_WhenSuccessful() {
        RefreshToken refreshToken = createRefreshToken();

        refreshToken.setUser(findUserByEmail("admin@mail.com"));

        RefreshToken expectedRefreshToken = refreshTokenRepository.save(refreshToken);

        ResponseEntity<PageableResponse<UserTokenResponse>> entity = httpClient.exchange(
                "/users/my/tokens",
                HttpMethod.GET,
                jwtCreator.createAdminAuthEntity(null),
                new ParameterizedTypeReference<>() {
                });

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(entity.getBody())
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(1);

        assertThat(entity.getBody().getContent()).isNotEmpty();

        assertThat(entity.getBody().getContent().get(0).getId()).isEqualTo(expectedRefreshToken.getId().toString());

        assertThat(entity.getBody().getContent().get(0).getToken()).isEqualTo(expectedRefreshToken.getToken());
    }

    @Test
    @DisplayName("promote Updates User Roles When Successful")
    void promote_UpdatesUserRoles_WhenSuccessful() {
        PromoteRequest promoteRequest = PromoteRequest
                .builder()
                .roles(Set.of("mod"))
                .userId(findUserByEmail("user@mail.com").getId().toString())
                .build();

        ResponseEntity<Void> entity = httpClient.exchange(
                "/users/promote",
                HttpMethod.PATCH,
                jwtCreator.createAdminAuthEntity(promoteRequest),
                Void.class
        );

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(entity.getBody()).isNull();
    }

    @Test
    @DisplayName("logout Removes Refresh Token When Successful")
    void logout_RemovesRefreshToken_WhenSuccessful() {
        String userId = findUserByEmail("user@mail.com").getId().toString();

        ResponseEntity<Void> entity = httpClient.exchange(
                "/users/logout/{userId}",
                HttpMethod.DELETE,
                jwtCreator.createAdminAuthEntity(null),
                Void.class,
                userId
        );

        assertThat(entity).isNotNull();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(entity.getBody()).isNull();
    }

    private User findUserByEmail(String email) throws RuntimeException {
        return userRepository
                .findByEmail(email)
                .orElseThrow(RuntimeException::new);
    }

}
