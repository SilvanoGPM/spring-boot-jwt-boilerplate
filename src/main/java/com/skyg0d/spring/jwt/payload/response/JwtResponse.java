package com.skyg0d.spring.jwt.payload.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class JwtResponse {

    @Schema(description = "Token to access protected endpoints")
    private String token;
    @Schema(description = "Type of token")
    private String type = "Bearer";
    @Schema(description = "Token to generate others access tokens")
    private String refreshToken;
    private String id;
    private String username;
    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken, String refreshToken, String id, String username, String email, List<String> roles) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public JwtResponse(String accessToken, String type, String refreshToken, String id, String username, String email, List<String> roles) {
        this(accessToken, refreshToken, id, username, email, roles);

        if (type == null) {
            type = "Bearer";
        }

        this.type = type;
    }

}
