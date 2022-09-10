package com.skyg0d.spring.jwt.service;

import com.skyg0d.spring.jwt.exception.TokenRefreshException;
import com.skyg0d.spring.jwt.model.RefreshToken;
import com.skyg0d.spring.jwt.model.User;
import com.skyg0d.spring.jwt.payload.UserMachineDetails;
import com.skyg0d.spring.jwt.payload.response.UserTokenResponse;
import com.skyg0d.spring.jwt.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Value("${app.jwt.refreshExpirationMs: #{0L}}")
    private Long refreshTokenDurationMs = 0L;

    public Page<RefreshToken> listAll(Pageable pageable) {
        return refreshTokenRepository.findAll(pageable);
    }

    public Page<UserTokenResponse> listAllByUser(Pageable pageable, UUID userId) {
        User user = userService.findById(userId);

        return refreshTokenRepository.findAllByUser(pageable, user).map((UserTokenResponse::new));
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken create(UUID userId, UserMachineDetails userMachineDetails) {
        User user = userService.findById(userId);

        RefreshToken refreshToken = RefreshToken
                .builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .ipAddress(userMachineDetails.getIpAddress())
                .browser(userMachineDetails.getBrowser())
                .operatingSystem(userMachineDetails.getOperatingSystem())
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        boolean isTokenExpired = token.getExpiryDate().compareTo(Instant.now()) < 0;

        if (isTokenExpired) {
            refreshTokenRepository.delete(token);

            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Transactional
    public void deleteByUserId(UUID userId) {
        refreshTokenRepository.deleteByUser(userService.findById(userId));
    }

}
