package com.skyg0d.spring.jwt.service;

import com.skyg0d.spring.jwt.exception.ResourceNotFoundException;
import com.skyg0d.spring.jwt.model.User;
import com.skyg0d.spring.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long id) throws ResourceNotFoundException {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

}
