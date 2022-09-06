package com.skyg0d.spring.jwt.controller;

import com.skyg0d.spring.jwt.model.User;
import com.skyg0d.spring.jwt.payload.request.PromoteRequest;
import com.skyg0d.spring.jwt.payload.response.MessageResponse;
import com.skyg0d.spring.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Page<User>> listAll(Pageable pageable) {
        return ResponseEntity.ok(userService.listAll(pageable));
    }

    @PatchMapping("/promote")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> promote(@Valid @RequestBody PromoteRequest request) {
        return ResponseEntity.ok(userService.promote(request.getUserId(), request.getRoles()));
    }

}
