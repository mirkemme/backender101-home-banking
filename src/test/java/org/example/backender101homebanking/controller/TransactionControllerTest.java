package org.example.backender101homebanking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.example.backender101homebanking.dto.TransactionDTO;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;

import static org.example.backender101homebanking.utils.TestObjectFactory.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    @DisplayName("POST /api/v1/transactions/withdraw - Success")
    public void testWithdraw() throws Exception {
        User user = buildUser("name-user1", "surname-user1", "123456789", "user1@email.com");
        Account account = buildAccount("ACC001", new BigDecimal("1000.00"), Collections.singletonList(user));

        Transaction transaction1 = buildTransaction(account, new BigDecimal("100.00"), Transaction.CurrencyType.EURO, Transaction.TransactionType.WITHDRAW);
        Transaction transaction2 = buildTransaction(account, new BigDecimal("200.00"), Transaction.CurrencyType.EURO, Transaction.TransactionType.WITHDRAW);

        entityManager.merge(user);
        entityManager.merge(account);
        entityManager.merge(transaction1);
        entityManager.merge(transaction2);
        entityManager.flush();

        TransactionDTO requestTransactionDTO = buildTransactionDTO("ACC001", "withdraw", new BigDecimal("300.00"), "euro");

        mockMvc.perform(post("/api/v1/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestTransactionDTO)))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.amount").value(-300.00))
                .andExpect(jsonPath("$.type").value("withdraw"));
    }

    @Test
    @Transactional
    @DisplayName("POST /api/v1/transactions/deposit - Success")
    public void testDeposit() throws Exception {
        User user = buildUser("name-user1", "surname-user1", "123456789", "user1@email.com");
        Account account = buildAccount("ACC001", new BigDecimal("1000.00"), Collections.singletonList(user));

        Transaction transaction1 = buildTransaction(account, new BigDecimal("100.00"), Transaction.CurrencyType.EURO, Transaction.TransactionType.DEPOSIT);
        Transaction transaction2 = buildTransaction(account, new BigDecimal("200.00"), Transaction.CurrencyType.EURO, Transaction.TransactionType.DEPOSIT);

        entityManager.merge(user);
        entityManager.merge(account);
        entityManager.merge(transaction1);
        entityManager.merge(transaction2);
        entityManager.flush();

        TransactionDTO requestTransactionDTO = buildTransactionDTO("ACC001", "deposit", new BigDecimal("300.00"), "euro");

        mockMvc.perform(post("/api/v1/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestTransactionDTO)))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.amount").value(300.00))
                .andExpect(jsonPath("$.type").value("deposit"));
    }

    /********** FAILURE TESTS **********/

    @Test
    @Transactional
    @DisplayName("POST /api/v1/transactions/withdraw - Failure: Account not found")
    public void testWithdrawFailureAccountIdNotFound() throws Exception {
        User user = buildUser("name-user1", "surname-user1", "123456789", "user1@email.com");
        Account account = buildAccount("ACC001", new BigDecimal("1000.00"), Collections.singletonList(user));

        entityManager.merge(user);
        entityManager.merge(account);
        entityManager.flush();

        TransactionDTO requestTransactionDTO = buildTransactionDTO("notExistingAccountNumber", "withdraw", new BigDecimal("300.00"), "euro");

        mockMvc.perform(post("/api/v1/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestTransactionDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @DisplayName("POST /api/v1/transactions/deposit - Failure: Account not found")
    public void testDepositFailureAccountIdNotFound() throws Exception {
        User user = buildUser("name-user1", "surname-user1", "123456789", "user1@email.com");
        Account account = buildAccount("ACC001", new BigDecimal("1000.00"), Collections.singletonList(user));

        entityManager.merge(user);
        entityManager.merge(account);
        entityManager.flush();

        TransactionDTO requestTransactionDTO = buildTransactionDTO("notExistingAccountNumber", "withdraw", new BigDecimal("300.00"), "euro");

        mockMvc.perform(post("/api/v1/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestTransactionDTO)))
                .andExpect(status().isNotFound());
    }
}
