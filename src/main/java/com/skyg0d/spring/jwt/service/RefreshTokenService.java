package com.skyg0d.spring.jwt.service;

import com.skyg0d.spring.jwt.exception.TokenRefreshException;
import com.skyg0d.spring.jwt.model.RefreshToken;
import com.skyg0d.spring.jwt.model.User;
import com.skyg0d.spring.jwt.payload.response.UserTokenResponse;
import com.skyg0d.spring.jwt.repository.RefreshTokenRepository;
import com.skyg0d.spring.jwt.repository.UserRepository;
import eu.bitwalker.useragentutils.UserAgent;
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

    @Value("${app.jwt.refreshExpirationMs}")
    private Long refreshTokenDurationMs;

    public Page<UserTokenResponse> listAllByUser(Pageable pageable, Long userId) {
        User user = userService.findById(userId);

        return refreshTokenRepository.findAllByUser(pageable, user).map((UserTokenResponse::new));
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId, UserAgent userAgent, String ip) {
        User user = userService.findById(userId);

        RefreshToken refreshToken = RefreshToken
                .builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .ipAddress(ip)
                .browser(userAgent.getBrowser().getName())
                .operatingSystem(userAgent.getOperatingSystem().getName())
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
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUser(userService.findById(userId));
    }

}
