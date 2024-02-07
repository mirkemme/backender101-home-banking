package org.example.backender101homebanking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.example.backender101homebanking.MockMvcRestExceptionConfiguration;
import org.example.backender101homebanking.dto.UserDTO;
import org.example.backender101homebanking.model.Account;
import org.example.backender101homebanking.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;

import static org.example.backender101homebanking.utils.TestObjectFactory.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {MockMvcRestExceptionConfiguration.class})
public class UserControllerTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    @DisplayName("GET /api/v1/users/all - Success")
    public void testGetAllUsers() throws Exception {
        User user1 = buildUser("name-user1", "surname-user1", "123456789", "user1@email.com");
        User user2 = buildUser("name-user2", "surname-user2", "987654321", "user2@email.com");
        Account account1 = buildAccount("ACC001", new BigDecimal("1000.00"), Collections.singletonList(user1));
        Account account2 = buildAccount("ACC002", new BigDecimal("2000.00"), Collections.singletonList(user2));

        entityManager.merge(user1);
        entityManager.merge(user2);
        entityManager.merge(account1);
        entityManager.merge(account2);
        entityManager.flush();

        mockMvc.perform(get("/api/v1/users/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("name-user1"))
                .andExpect(jsonPath("$[0].lastName").value("surname-user1"))
                .andExpect(jsonPath("$[1].firstName").value("name-user2"))
                .andExpect(jsonPath("$[1].lastName").value("surname-user2"));
    }

    @Test
    @Transactional
    @DisplayName("GET /api/v1/users/{userId} - Success")
    public void testGetUserById() throws Exception {
        User user = buildUser("name-user1", "surname-user1", "123456789", "user1@email.com");
        Account account = buildAccount("ACC001", new BigDecimal("1000.00"), Collections.singletonList(user));

        entityManager.merge(user);
        entityManager.merge(account);
        entityManager.flush();

        mockMvc.perform(get("/api/v1/users/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("name-user1"))
                .andExpect(jsonPath("$.lastName").value("surname-user1"));
    }

    @Test
    @Transactional
    @DisplayName("POST /api/v1/users - Success")
    public void testAddUser() throws Exception {
        UserDTO requestUserDTO = buildUserDTO("name-user1", "surname-user1", "123123123", "user1@email.com");

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("name-user1"))
                .andExpect(jsonPath("$.lastName").value("surname-user1"))
                .andExpect(jsonPath("$.email").value("user1@email.com"));
    }

    @Test
    @Transactional
    @DisplayName("PUT /api/v1/users/{userId} - Success")
    public void testUpdateUser() throws Exception {
        User existingUser = buildUser("name-user1", "surname-user1", "123456789", "user1@email.com");
        Account existingAccount = buildAccount("ACC001", new BigDecimal("1000.00"), Collections.singletonList(existingUser));

        entityManager.merge(existingUser);
        entityManager.merge(existingAccount);
        entityManager.flush();

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setFirstName("updated-name");
        updatedUserDTO.setLastName("updated-surname");
        updatedUserDTO.setEmail(existingUser.getEmail());
        updatedUserDTO.setPassword(existingUser.getPassword());

        mockMvc.perform(put("/api/v1/users/{userId}", existingUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatedUserDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("updated-name"))
                .andExpect(jsonPath("$.lastName").value("updated-surname"));
    }

    @Test
    @Transactional
    @DisplayName("DELETE /api/v1/users/{userId} - Success")
    public void testDeleteUser() throws Exception {
        User existingUser = buildUser("name-user1", "surname-user1", "123456789", "user1@email.com");
        Account existingAccount = buildAccount("ACC001", new BigDecimal("1000.00"), Collections.singletonList(existingUser));

        entityManager.merge(existingUser);
        entityManager.merge(existingAccount);
        entityManager.flush();

        mockMvc.perform(delete("/api/v1/users/{userId}", existingUser.getId()))
                .andExpect(status().isOk());
    }

    /********** FAILURE TESTS **********/

    @Test
    @DisplayName("GET /api/v1/users/{userId} - Failure: Resource Not Found")
    public void testGetUserByIdFailure() throws Exception {
        int notExistingUserId = 123456;
        mockMvc.perform(get("/api/v1/users/{userId}", notExistingUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("User not found with id: 123456"));
    }

    @Test
    @Transactional
    @DisplayName("POST /api/v1/users - Failure: Bad Request")
    public void testAddUserFailure() throws Exception {
        User existingUser = buildUser("name-user1", "surname-user1", "123456789", "user1@email.com");
        Account existingAccount = buildAccount("ACC001", new BigDecimal("1000.00"), Collections.singletonList(existingUser));

        entityManager.merge(existingUser);
        entityManager.merge(existingAccount);
        entityManager.flush();

        UserDTO requestUserDTO = buildUserDTO("name-user2", "surname-user2", "123123123", "user1@email.com");

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(new ObjectMapper().writeValueAsString(requestUserDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @Transactional
    @DisplayName("PUT /api/v1/users/{userId} - Failure: Resource Not Found")
    public void testUpdateUserFailure() throws Exception {
        User existingUser = buildUser("name-user1", "surname-user1", "123456789", "user1@email.com");
        Account existingAccount = buildAccount("ACC001", new BigDecimal("1000.00"), Collections.singletonList(existingUser));

        entityManager.merge(existingUser);
        entityManager.merge(existingAccount);
        entityManager.flush();

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setFirstName("updated-name");
        updatedUserDTO.setLastName("updated-surname");
        updatedUserDTO.setEmail(existingUser.getEmail());
        updatedUserDTO.setPassword(existingUser.getPassword());

        int notExistingUserId = 123456;
        mockMvc.perform(put("/api/v1/users/{userId}", notExistingUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedUserDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("User not found with id: 123456"));
    }

    @Test
    @Transactional
    @DisplayName("DELETE /api/v1/users/{userId} - Failure: Resource Not Found")
    public void testDeleteUser_ResourceNotFound() throws Exception {
        int notExistingUserId = 123456;
        mockMvc.perform(delete("/api/v1/users/{userId}", notExistingUserId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("User not found with id: 123456"));
    }
}
