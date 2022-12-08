package com.skyg0d.spring.jwt.controller;

import com.skyg0d.spring.jwt.model.RefreshToken;
import com.skyg0d.spring.jwt.model.User;
import com.skyg0d.spring.jwt.payload.request.PromoteRequest;
import com.skyg0d.spring.jwt.payload.response.UserTokenResponse;
import com.skyg0d.spring.jwt.security.service.UserDetailsImpl;
import com.skyg0d.spring.jwt.service.AuthService;
import com.skyg0d.spring.jwt.service.RefreshTokenService;
import com.skyg0d.spring.jwt.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

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
    @Operation(summary = "Returns all users with pagination", tags = "Users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "401", description = "When not authorized"),
            @ApiResponse(responseCode = "403", description = "When forbidden"),
            @ApiResponse(responseCode = "500", description = "When server error")
    })
    public ResponseEntity<Page<User>> listAll(Pageable pageable) {
        return ResponseEntity.ok(userService.listAll(pageable));
    }

    @GetMapping("/tokens")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    @Operation(summary = "Returns all users tokens with pagination", tags = "Users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "401", description = "When not authorized"),
            @ApiResponse(responseCode = "403", description = "When forbidden"),
            @ApiResponse(responseCode = "500", description = "When server error")
    })
    public ResponseEntity<Page<RefreshToken>> listAllTokens(Pageable pageable) {
        return ResponseEntity.ok(refreshTokenService.listAll(pageable));
    }

    @GetMapping("/my/tokens")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @Operation(summary = "Returns all user tokens with pagination", tags = "Users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "401", description = "When not authorized"),
            @ApiResponse(responseCode = "500", description = "When server error")
    })
    public ResponseEntity<Page<UserTokenResponse>> listMyAllTokens(Pageable pageable) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return ResponseEntity.ok(refreshTokenService.listAllByUser(pageable, userDetails.getId()));
    }

    @PatchMapping("/promote")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Promote user to others roles", tags = "Users")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful"),
            @ApiResponse(responseCode = "400", description = "When user not found"),
            @ApiResponse(responseCode = "401", description = "When not authorized"),
            @ApiResponse(responseCode = "403", description = "When forbidden"),
            @ApiResponse(responseCode = "500", description = "When server error")
    })
    public ResponseEntity<Void> promote(@Valid @RequestBody PromoteRequest request) {
        userService.promote(UUID.fromString(request.getUserId()), request.getRoles());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/logout/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "User logout", tags = "Users")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Successful"),
            @ApiResponse(responseCode = "400", description = "When user not found"),
            @ApiResponse(responseCode = "401", description = "When not authorized"),
            @ApiResponse(responseCode = "403", description = "When forbidden"),
            @ApiResponse(responseCode = "500", description = "When server error")
    })
    public ResponseEntity<Void> logout(@PathVariable UUID userId) {
        authService.logout(userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
