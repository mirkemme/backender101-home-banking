package org.example.backender101homebanking.utils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backender101homebanking.model.Role;
import org.example.backender101homebanking.model.enums.ERole;
import org.example.backender101homebanking.repository.RoleRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/* class designed to populate the database with roles if they don't already exist
during application startup. */
@Component
@RequiredArgsConstructor
public class RoleDataSeeder {
    private final RoleRepository roleRepository;

    @EventListener
    @Transactional
    public void LoadRoles(ContextRefreshedEvent event) {

        List<ERole> roles = Arrays.stream(ERole.values()).toList();

        for(ERole erole: roles) {
            if (roleRepository.findByName(erole)==null) {
                roleRepository.save(new Role(erole));
            }
        }

    }
}
