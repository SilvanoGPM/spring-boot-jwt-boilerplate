package com.skyg0d.spring.jwt.service;

import com.skyg0d.spring.jwt.model.ERole;
import com.skyg0d.spring.jwt.model.Role;
import com.skyg0d.spring.jwt.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {

    final RoleRepository roleRepository;

    public Role findByName(ERole name) throws RuntimeException {
        return roleRepository
                .findByName(name)
                .orElseThrow(() -> new RuntimeException("Role is not found."));
    }

}
