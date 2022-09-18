package com.skyg0d.spring.jwt.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenRefreshRequest {

    @NotBlank
    @Schema(description = "Refresh Token to generate more tokens")
    private String refreshToken;

}
