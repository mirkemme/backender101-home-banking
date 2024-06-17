package org.example.backender101homebanking.controller;

import jakarta.persistence.EntityManager;
import org.example.backender101homebanking.dto.AccountDTO;
import org.example.backender101homebanking.model.Account;
import org.example.backender101homebanking.model.Transaction;
import org.example.backender101homebanking.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.example.backender101homebanking.utils.TestObjectFactory.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AccountControllerTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    @DisplayName("GET /api/v1/accounts/all - Success")
    public void testGetAllAccounts() throws Exception {
        User user1 = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");
        User user2 = buildUser("name-user2", "surname-user2", "username2", "user2@email.com", "987654321");
        Account account1 = buildAccount("IT60X0542811101000000654321", new BigDecimal("1000.00"), Collections.singletonList(user1));
        Account account2 = buildAccount("IT60X0542811101000000654322", new BigDecimal("2000.00"), Collections.singletonList(user2));

        entityManager.merge(user1);
        entityManager.merge(user2);
        entityManager.merge(account1);
        entityManager.merge(account2);
        entityManager.flush();

        mockMvc.perform(get("/api/v1/accounts/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].number").value("IT60X0542811101000000654321"))
                .andExpect(jsonPath("$[0].balance").value(1000))
                .andExpect(jsonPath("$[1].number").value("IT60X0542811101000000654322"))
                .andExpect(jsonPath("$[1].balance").value(2000));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/v1/accounts/{accountNumber}/balance - Success")
    public void testGetAccountBalance() throws Exception {
        User user = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");
        Account account = buildAccount("IT60X0542811101000000654321", new BigDecimal("1000.00"), Collections.singletonList(user));

        entityManager.merge(user);
        entityManager.merge(account);
        entityManager.flush();

        mockMvc.perform(get("/api/v1/accounts/{accountNumber}/balance", account.getNumber())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.balance").value(1000));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/v1/accounts - Success")
    public void testCreateAccount() throws Exception {
        AccountDTO requestAccountDTO = new AccountDTO();
        requestAccountDTO.setBalance(new BigDecimal("3000.00"));
        List<Long> userIds = Arrays.asList(1L, 2L);
        requestAccountDTO.setUserIds(userIds);

        User user1 = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");
        User user2 = buildUser("name-user2", "surname-user2", "username2", "user2@email.com", "987654321");

        Account account1 = buildAccount(
                "IT60X0542811101000000654321",
                new BigDecimal("1000.00"),
                Collections.singletonList(user1));

        Account account2 = buildAccount(
                "IT60X0542811101000000654322",
                new BigDecimal("2000.00"),
                Collections.singletonList(user2));

        entityManager.merge(user1);
        entityManager.merge(user2);
        entityManager.merge(account1);
        entityManager.merge(account2);
        entityManager.flush();

        mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"balance\": 2000," +
                                "\"userIds\": [1, 2]}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/v1/accounts/{accountNumber}/transactions - Success")
    public void testGetLast5Transactions() throws Exception {
        User user = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");
        Account account = buildAccount("IT60X0542811101000000654321", new BigDecimal("1000.00"), Collections.singletonList(user));

        Transaction transaction1 = buildTransaction(account, new BigDecimal("100.00"), Transaction.CurrencyType.EURO, Transaction.TransactionType.DEPOSIT);
        Transaction transaction2 = buildTransaction(account, new BigDecimal("50.00"), Transaction.CurrencyType.EURO, Transaction.TransactionType.WITHDRAW);

        entityManager.merge(user);
        entityManager.merge(account);
        entityManager.merge(transaction1);
        entityManager.merge(transaction2);
        entityManager.flush();

        mockMvc.perform(get("/api/v1/accounts/{accountNumber}/transactions", account.getNumber())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].amount").value(100.00))
                .andExpect(jsonPath("$[1].amount").value(50.00));
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/v1/accounts/{accountNumber} - Success")
    public void testDeleteAccount() throws Exception {
        User user = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");
        Account accountToDelete = buildAccount("IT60X0542811101000000654321", new BigDecimal("3000.00"), Collections.singletonList(user));

        entityManager.merge(user);
        entityManager.merge(accountToDelete);
        entityManager.flush();

        mockMvc.perform(delete("/api/v1/accounts/{accountNumber}", accountToDelete.getNumber()))
                .andExpect(status().isOk());
    }

    /********** FAILURE TESTS **********/

    @Test
    @WithMockUser
    @DisplayName("GET /api/v1/accounts/all - Failure: Resource Not Found")
    public void testGetAllAccountsFailure() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/all/invalidPath")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/v1/accounts/{accountNumber}/balance - ResourceNotFoundException")
    public void testGetAccountBalanceFailure() throws Exception {
        String nonExistingAccountNumber = "IT60X0542811101000000654320";
        mockMvc.perform(get("/api/v1/accounts/{accountNumber}/balance", nonExistingAccountNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/v1/accounts - Failure: User IDs not found")
    public void testCreateAccountFailureUserIdsNotFound() throws Exception {
        User user = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");
        Account accountToDelete = buildAccount("IT60X0542811101000000654321", new BigDecimal("3000.00"), Collections.singletonList(user));

        entityManager.merge(user);
        entityManager.merge(accountToDelete);
        entityManager.flush();

        mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"balance\": 2000," +
                                "\"userIds\": [12345]}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(404));
    }
}
