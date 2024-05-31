package org.example.backender101homebanking.repository;

import org.example.backender101homebanking.model.Role;
import org.example.backender101homebanking.model.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(ERole name);
}
