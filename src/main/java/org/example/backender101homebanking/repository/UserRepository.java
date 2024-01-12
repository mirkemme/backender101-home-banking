package org.example.backender101homebanking.repository;

import org.example.backender101homebanking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("mysql-user")
public interface UserRepository extends JpaRepository<User, Integer> {
}