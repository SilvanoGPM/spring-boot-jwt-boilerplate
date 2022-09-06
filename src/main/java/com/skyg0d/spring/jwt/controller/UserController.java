package com.skyg0d.spring.jwt.controller;

import com.skyg0d.spring.jwt.model.RefreshToken;
import com.skyg0d.spring.jwt.model.User;
import com.skyg0d.spring.jwt.payload.request.PromoteRequest;
import com.skyg0d.spring.jwt.payload.response.MessageResponse;
import com.skyg0d.spring.jwt.payload.response.UserTokenResponse;
import com.skyg0d.spring.jwt.security.service.UserDetailsImpl;
import com.skyg0d.spring.jwt.service.AuthService;
import com.skyg0d.spring.jwt.service.RefreshTokenService;
import com.skyg0d.spring.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    final UserService userService;

    final AuthService authService;
    final RefreshTokenService refreshTokenService;

    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Page<User>> listAll(Pageable pageable) {
        return ResponseEntity.ok(userService.listAll(pageable));
    }

    @GetMapping("/tokens")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Page<RefreshToken>> listMyAllTokens(Pageable pageable) {
        return ResponseEntity.ok(refreshTokenService.listAll(pageable));
    }

    @GetMapping("/my/tokens")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Page<UserTokenResponse>> listMyAllTokens(Pageable pageable, Principal principal) {
        UserDetailsImpl userDetails = (UserDetailsImpl) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        return ResponseEntity.ok(refreshTokenService.listAllByUser(pageable, userDetails.getId()));
    }

    @PatchMapping("/promote")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> promote(@Valid @RequestBody PromoteRequest request) {
        return ResponseEntity.ok(userService.promote(request.getUserId(), request.getRoles()));
    }

    @DeleteMapping("/logout/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> logout(@PathVariable Long userId) {
        return ResponseEntity.ok(authService.logout(userId));
    }

}
