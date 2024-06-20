package org.example.backender101homebanking.controller;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import org.example.backender101homebanking.MockMvcRestExceptionConfiguration;
import org.example.backender101homebanking.model.Account;
import org.example.backender101homebanking.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;

import static org.example.backender101homebanking.utils.TestObjectFactory.buildAccount;
import static org.example.backender101homebanking.utils.TestObjectFactory.buildUser;
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
    @MockBean
    HttpServletRequest request;

    @Test
    @Transactional
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /api/v1/users/all - Success")
    public void testGetAllUsers() throws Exception {
        User user1 = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");
        User user2 = buildUser("name-user2", "surname-user2", "username2", "user2@email.com", "987654321");
        Account account1 = buildAccount("IT60X0542811101000000654321", new BigDecimal("1000.00"), Collections.singletonList(user1));
        Account account2 = buildAccount("IT60X0542811101000000654322", new BigDecimal("2000.00"), Collections.singletonList(user2));

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
    @WithMockUser
    @DisplayName("GET /api/v1/users/me - Success")
    public void testGetUserById() throws Exception {
        User user = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");
        Account account = buildAccount("IT60X0542811101000000654321", new BigDecimal("1000.00"), Collections.singletonList(user));

        entityManager.merge(user);
        entityManager.merge(account);
        entityManager.flush();

        mockMvc.perform(get("/api/v1/users/me")
                        .requestAttr("userId", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("name-user1"))
                .andExpect(jsonPath("$.lastName").value("surname-user1"));
    }

    @Test
    @Transactional
    @WithMockUser
    @DisplayName("POST /api/v1/users/auth/signup - Success")
    public void testAddUser() throws Exception {
        User user = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");
        entityManager.merge(user);
        entityManager.flush();

        mockMvc.perform(post("/api/v1/users/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"name-user2\"," +
                                "\"lastName\":\"surname-user2\"," +
                                "\"username\":\"username2\"," +
                                "\"password\":\"123456789\"," +
                                "\"email\":\"user2@email.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User account has been successfully created!"));
    }

    @Test
    @Transactional
    @WithMockUser
    @DisplayName("POST /api/v1/users/auth/signin - Success")
    public void testSignInUser() throws Exception {
        User user = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");
        entityManager.merge(user);
        entityManager.flush();

        mockMvc.perform(post("/api/v1/users/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user1@email.com\"," +
                                "\"password\":\"123456789\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Sign in successful!"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "name-user1", roles = "USER")
    @DisplayName("PUT /api/v1/users/update - Success")
    public void testUpdateUser() throws Exception {
        User existingUser = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");
        Account existingAccount = buildAccount("IT60X0542811101000000654321", new BigDecimal("1000.00"), Collections.singletonList(existingUser));

        entityManager.merge(existingUser);
        entityManager.merge(existingAccount);
        entityManager.flush();

        mockMvc.perform(put("/api/v1/users/update")
                        .requestAttr("userId", existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"name-user\"," +
                                "\"lastName\":\"surname-user\"," +
                                "\"username\":\"updated-username\"," +
                                "\"password\":\"123456789\"," +
                                "\"email\":\"user@email.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updated-username"));
    }

    @Test
    @Transactional
    @WithMockUser
    @DisplayName("DELETE /api/v1/users/delete - Success")
    public void testDeleteUser() throws Exception {
        User existingUser = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");
        Account existingAccount = buildAccount("IT60X0542811101000000654321", new BigDecimal("1000.00"), Collections.singletonList(existingUser));

        entityManager.merge(existingUser);
        entityManager.merge(existingAccount);
        entityManager.flush();

        mockMvc.perform(delete("/api/v1/users/delete")
                .requestAttr("userId", existingUser.getId()))
                .andExpect(status().isOk());
    }

    /********** FAILURE TESTS **********/

    @Test
    @Transactional
    @WithMockUser
    @DisplayName("GET /api/v1/users/me - Failure: Resource Not Found")
    public void testGetUserByIdFailure() throws Exception {
        User existingUser = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");
        Account existingAccount = buildAccount("IT60X0542811101000000654321", new BigDecimal("1000.00"), Collections.singletonList(existingUser));

        entityManager.merge(existingUser);
        entityManager.merge(existingAccount);
        entityManager.flush();

        long notExistingUserId = 123456L;
        mockMvc.perform(get("/api/v1/users/me")
                        .requestAttr("userId", notExistingUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("User not found with id: 123456"));
    }

    @Test
    @Transactional
    @WithMockUser
    @DisplayName("POST /api/v1/users/auth/signup - Failure: is conflict")
    public void testAddUserFailure() throws Exception {
        User existingUser = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");
        Account existingAccount = buildAccount("IT60X0542811101000000654321", new BigDecimal("1000.00"), Collections.singletonList(existingUser));

        entityManager.merge(existingUser);
        entityManager.merge(existingAccount);
        entityManager.flush();

        mockMvc.perform(post("/api/v1/users/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"name-user2\"," +
                                "\"lastName\":\"surname-user2\"," +
                                "\"username\":\"username2\"," +
                                "\"password\":\"123456789\"," +
                                "\"email\":\"user1@email.com\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Registration Failed: Provided email already exists. Try sign in or provide another email."));
    }

    @Test
    @Transactional
    @WithMockUser
    @DisplayName("POST /api/v1/users/auth/signin - Failure: unauthorized")
    public void testSignInFailure() throws Exception {
        User existingUser = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");
        Account existingAccount = buildAccount("IT60X0542811101000000654321", new BigDecimal("1000.00"), Collections.singletonList(existingUser));

        entityManager.merge(existingUser);
        entityManager.merge(existingAccount);
        entityManager.flush();

        mockMvc.perform(post("/api/v1/users/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"wrong@email.com\"," +
                                "\"password\":\"123456789\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Bad credentials"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "name-user1", roles = "USER")
    @DisplayName("PUT /api/v1/users/update - Failure: Resource Not Found")
    public void testUpdateUserFailure() throws Exception {
        long notExistingUserId = 123456L;
        User existingUser = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");
        Account existingAccount = buildAccount("IT60X0542811101000000654321", new BigDecimal("1000.00"), Collections.singletonList(existingUser));

        entityManager.merge(existingUser);
        entityManager.merge(existingAccount);
        entityManager.flush();

        mockMvc.perform(put("/api/v1/users/update")
                        .requestAttr("userId", notExistingUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"name-user1\"," +
                                "\"lastName\":\"surname-user1\"," +
                                "\"username\":\"username1\"," +
                                "\"password\":\"123456789\"," +
                                "\"email\":\"user1-updated@email.com\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("User not found with id: 123456"));
    }

    @Test
    @Transactional
    @WithMockUser
    @DisplayName("DELETE /api/v1/users/delete - Failure: Resource Not Found")
    public void testDeleteUser_ResourceNotFound() throws Exception {
        long notExistingUserId = 123456L;
        mockMvc.perform(delete("/api/v1/users/delete")
                .requestAttr("userId", notExistingUserId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("User not found with id: 123456"));
    }
}
