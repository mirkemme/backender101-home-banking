package org.example.backender101homebanking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static org.example.backender101homebanking.utils.TestObjectFactory.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    @DisplayName("GET /api/v1/accounts/all - Success")
    public void testGetAllAccounts() throws Exception {
        Account account1 = buildAccount("ACC001", new BigDecimal("1000.00"));
        Account account2 = buildAccount("ACC002", new BigDecimal("2000.00"));

        User user1 = buildUser( "name-user1", "surname-user1", "123456789", "user1@email.com", Collections.singletonList(account1));
        User user2 = buildUser( "name-user2", "surname-user2", "987654321", "user2@email.com", Collections.singletonList(account2));

        mockMvc.perform(get("/api/v1/accounts/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].number").value("ACC001"))
                .andExpect(jsonPath("$[0].balance").value(1000))
                .andExpect(jsonPath("$[1].number").value("ACC002"))
                .andExpect(jsonPath("$[1].balance").value(2000));
    }

    @Test
    @Transactional
    @DisplayName("GET /api/v1/accounts/{accountNumber}/balance - Success")
    public void testGetAccountBalance() throws Exception {
        Account account = buildAccount("ACC001", new BigDecimal("1000.00"));
        User user = buildUser( "name-user1", "surname-user1", "123456789", "user1@email.com", Collections.singletonList(account));

        mockMvc.perform(get("/api/v1/accounts/{accountNumber}/balance", account.getNumber())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.balance").value(1000));
    }

    @Test
    @Transactional
    @DisplayName("POST /api/v1/accounts - Success")
    public void testCreateAccount() throws Exception {
        AccountDTO requestAccountDTO = new AccountDTO();
        requestAccountDTO.setNumber("ACC003");
        requestAccountDTO.setBalance(new BigDecimal("3000.00"));
        List<Integer> userIds = Arrays.asList(1, 2);
        requestAccountDTO.setUserIds(userIds);

        Account account1 = buildAccount("ACC001", new BigDecimal("1000.00"));
        Account account2 = buildAccount("ACC002", new BigDecimal("2000.00"));
        User user1 = buildUser( "name-user1", "surname-user1", "123456789", "user1@email.com", Collections.singletonList(account1));
        User user2 = buildUser( "name-user2", "surname-user2", "987654321", "user2@email.com", Collections.singletonList(account2));

        ResultActions result = mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestAccountDTO)));

                result.andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.number").value("ACC003"))
                .andExpect(jsonPath("$.balance").value(3000.00))
                .andExpect(jsonPath("$.userIds", hasSize(2)))
                .andExpect(jsonPath("$.userIds[0]").value(1))
                .andExpect(jsonPath("$.userIds[1]").value(2));
    }

    @Test
    @Transactional
    @DisplayName("GET /api/v1/accounts/{accountNumber}/transactions - Success")
    public void testGetLast5Transactions() throws Exception {
        Account account = buildAccount("ACC001", new BigDecimal("1000.00"));
        User user = buildUser("name-user1", "surname-user1", "123456789", "user1@email.com", Collections.singletonList(account));

        Transaction transaction1 = buildTransaction(account, Transaction.TransactionType.DEPOSIT, new BigDecimal("100.00"));
        Transaction transaction2 = buildTransaction(account, Transaction.TransactionType.WITHDRAW, new BigDecimal("50.00"));

        mockMvc.perform(get("/api/v1/accounts/{accountNumber}/transactions", account.getNumber())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[1].amount").value(100.00))
                .andExpect(jsonPath("$[0].amount").value(50.00));
    }

    @Test
    @Transactional
    @DisplayName("DELETE /api/v1/accounts/{accountNumber} - Success")
    public void testDeleteAccount() throws Exception {
        Account accountToDelete = buildAccount("ACC001", new BigDecimal("3000.00"));

        mockMvc.perform(delete("/api/v1/accounts/{accountNumber}", accountToDelete.getNumber()))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @DisplayName("GET /api/v1/accounts/all - Failure: Resource Not Found")
    public void testGetAllAccountsFailure() throws Exception {
        mockMvc.perform(get("/api/v1/accounts/all/invalidPath")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @DisplayName("GET /api/v1/accounts/{accountNumber}/balance - ResourceNotFoundException")
    public void testGetAccountBalanceFailure() throws Exception {
        String nonExistingAccountNumber = "ACC123456";
        mockMvc.perform(get("/api/v1/accounts/{accountNumber}/balance", nonExistingAccountNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @DisplayName("POST /api/v1/accounts - Failure (User IDs not found)")
    public void testCreateAccountFailureUserIdsNotFound() throws Exception {
        AccountDTO requestAccountDTO = new AccountDTO();
        requestAccountDTO.setNumber("ACC003");
        requestAccountDTO.setBalance(new BigDecimal("3000.00"));
        List<Integer> nonExistingUserIds = Arrays.asList(100, 200);
        requestAccountDTO.setUserIds(nonExistingUserIds);

        ResultActions result = mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestAccountDTO)));

        result.andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(404));
    }
}
