package org.example.backender101homebanking.repository;

import org.example.backender101homebanking.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindById() {
        User user = new User();
        user.setFirstName("Bart");
        user.setLastName("Simpson");
        user.setEmail("bartsimpson@example.com");
        user.setPassword("123456789");
        entityManager.persist(user);
        entityManager.flush();

        User foundUser = userRepository.findById(1).orElse(null);

        if (foundUser != null) {
            System.out.println("Found User: " + foundUser.getId() + " - " + foundUser.getFirstName());
            assertEquals(1, foundUser.getId());
            assertEquals("Bart", foundUser.getFirstName());
        } else {
            System.out.println("User not found!");
        }
    }
}

