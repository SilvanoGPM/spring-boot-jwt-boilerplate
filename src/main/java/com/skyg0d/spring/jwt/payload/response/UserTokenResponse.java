package com.skyg0d.spring.jwt.payload.response;

import com.skyg0d.spring.jwt.model.RefreshToken;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserTokenResponse {

    private String id;
    private Instant expiryDate;
    private String token;
    private String browser;
    private String operatingSystem;
    private String ipAddress;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public UserTokenResponse(RefreshToken refreshToken) {
        this.id = refreshToken.getId().toString();
        this.expiryDate = refreshToken.getExpiryDate();
        this.token = refreshToken.getToken();
        this.browser = refreshToken.getBrowser();
        this.operatingSystem = refreshToken.getOperatingSystem();
        this.ipAddress = refreshToken.getIpAddress();
        this.createdAt = refreshToken.getCreatedAt();
        this.updatedAt = refreshToken.getUpdatedAt();
    }

}
