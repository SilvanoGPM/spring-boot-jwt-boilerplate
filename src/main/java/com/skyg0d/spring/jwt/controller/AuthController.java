package com.skyg0d.spring.jwt.controller;

import com.skyg0d.spring.jwt.payload.request.LogOutRequest;
import com.skyg0d.spring.jwt.payload.request.LoginRequest;
import com.skyg0d.spring.jwt.payload.request.SignupRequest;
import com.skyg0d.spring.jwt.payload.request.TokenRefreshRequest;
import com.skyg0d.spring.jwt.payload.response.JwtResponse;
import com.skyg0d.spring.jwt.payload.response.MessageResponse;
import com.skyg0d.spring.jwt.payload.response.TokenRefreshResponse;
import com.skyg0d.spring.jwt.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> signIn(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.signIn(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signUp(@Valid @RequestBody SignupRequest signUpRequest) {
        return ResponseEntity.ok(authService.singUp(signUpRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<MessageResponse> logout(@Valid @RequestBody LogOutRequest logOutRequest) {
        return ResponseEntity.ok(authService.logout(logOutRequest.getUserId()));
    }

}
