package com.skyg0d.spring.jwt.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Builder
@Entity(name = "refreshtoken")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

}
