package com.skyg0d.spring.jwt.controller;

import com.skyg0d.spring.jwt.exception.BadRequestException;
import com.skyg0d.spring.jwt.payload.request.LoginRequest;
import com.skyg0d.spring.jwt.payload.request.SignupRequest;
import com.skyg0d.spring.jwt.payload.request.TokenRefreshRequest;
import com.skyg0d.spring.jwt.payload.response.JwtResponse;
import com.skyg0d.spring.jwt.payload.response.MessageResponse;
import com.skyg0d.spring.jwt.payload.response.TokenRefreshResponse;
import com.skyg0d.spring.jwt.security.service.UserDetailsImpl;
import com.skyg0d.spring.jwt.service.AuthService;
import com.skyg0d.spring.jwt.util.HttpUtils;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> signIn(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String ipAddress = HttpUtils.getClientIp();

        return ResponseEntity.ok(authService.signIn(loginRequest, userAgent, ipAddress));
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
    public ResponseEntity<MessageResponse> logout(Principal principal) {
        if (principal == null) {
            throw new BadRequestException("You are not logged in.");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        return ResponseEntity.ok(authService.logout(userDetails.getId()));
    }

}
