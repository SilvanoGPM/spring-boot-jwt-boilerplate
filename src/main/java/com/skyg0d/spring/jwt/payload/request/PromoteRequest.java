package com.skyg0d.spring.jwt.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PromoteRequest {

    @Schema(description = "Roles to promote user")
    private Set<String> roles;

    private String userId;

}
