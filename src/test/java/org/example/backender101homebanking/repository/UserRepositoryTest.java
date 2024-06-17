package org.example.backender101homebanking.repository;

import org.example.backender101homebanking.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.example.backender101homebanking.utils.TestObjectFactory.buildUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.properties")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findByEmail() {
        User user = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");

        entityManager.persist(user);
        entityManager.flush();

        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());

        if (foundUser.isPresent()) {
            System.out.println("Found User: " + foundUser.orElseThrow().getId() + " - " + foundUser.orElseThrow().getFirstName());
            assertEquals(1, foundUser.orElseThrow().getId());
            assertEquals("name-user1", foundUser.orElseThrow().getFirstName());
        } else {
            System.out.println("User not found!");
        }
    }
}

