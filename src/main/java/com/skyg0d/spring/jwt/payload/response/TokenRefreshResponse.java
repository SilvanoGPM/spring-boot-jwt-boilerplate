package com.skyg0d.spring.jwt.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenRefreshResponse {

    @Schema(description = "New token generated")
    private String accessToken;
    @Schema(description = "Token to generate others access tokens")
    private String refreshToken;

    @Schema(description = "Type of token")
    private String tokenType = "Bearer";

    public TokenRefreshResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
