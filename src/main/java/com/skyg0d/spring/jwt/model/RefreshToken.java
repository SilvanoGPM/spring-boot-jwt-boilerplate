package com.skyg0d.spring.jwt.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Setter
@Getter
@Entity(name = "refreshtoken")
public class RefreshToken extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Schema(description = "Owner of token")
    private User user;

    @Column(nullable = false, unique = true)
    @Schema(description = "Token value")
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @Schema(description = "Browser that requested the token")
    private String browser;

    @Schema(description = "Operating System that requested the token")
    private String operatingSystem;

    @Schema(description = "Ip Address that requested the token")
    private String ipAddress;

}
