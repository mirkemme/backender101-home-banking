package org.example.backender101homebanking.utils;

import lombok.RequiredArgsConstructor;
import org.example.backender101homebanking.exception.RoleNotFoundException;
import org.example.backender101homebanking.model.Role;
import org.example.backender101homebanking.model.enums.ERole;
import org.example.backender101homebanking.repository.RoleRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleFactory {

    private final RoleRepository roleRepository;

    public Role getInstance(String role) throws RoleNotFoundException {
        switch (role) {
            case "admin" -> {
                return roleRepository.findByName(ERole.ROLE_ADMIN);
            }
            case "user" -> {
                return roleRepository.findByName(ERole.ROLE_USER);
            }
            case "super_admin" -> {
                return roleRepository.findByName(ERole.ROLE_SUPER_ADMIN);
            }
            default -> throw new RoleNotFoundException("No role found for " +  role);
        }
    }
}
