package com.skyg0d.spring.jwt.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PromoteRequest {

    private Set<String> roles;
    private Long userId;

}
