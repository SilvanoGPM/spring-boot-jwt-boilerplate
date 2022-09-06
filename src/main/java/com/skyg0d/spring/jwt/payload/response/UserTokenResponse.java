package com.skyg0d.spring.jwt.payload.response;

import com.skyg0d.spring.jwt.model.RefreshToken;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserTokenResponse {

    private long id;
    private Instant expiryDate;
    private String token;
    private String browser;
    private String operatingSystem;
    private String ipAddress;

    public UserTokenResponse(RefreshToken refreshToken) {
        this.id = refreshToken.getId();
        this.expiryDate = refreshToken.getExpiryDate();
        this.token = refreshToken.getToken();
        this.browser = refreshToken.getBrowser();
        this.operatingSystem = refreshToken.getOperatingSystem();
        this.ipAddress = refreshToken.getIpAddress();
    }

}
