package org.example.backender101homebanking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.example.backender101homebanking.dto.AccountDTO;
import org.example.backender101homebanking.model.Account;
import org.example.backender101homebanking.model.Transaction;
import org.example.backender101homebanking.model.User;
import org.example.backender101homebanking.repository.AccountRepository;
import org.example.backender101homebanking.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    @DisplayName("GET /api/v1/accounts/all")
    public void testGetAllAccounts() throws Exception {
        Account account1 = createAccount("ACC001", new BigDecimal("1000.00"));
        Account account2 = createAccount("ACC002", new BigDecimal("2000.00"));

        User user1 = createUser(1, "name-user1", "surname-user1", "123456789", "user1@email.com", Collections.singletonList(account1));
        User user2 = createUser(2, "name-user2", "surname-user2", "987654321", "user2@email.com", Collections.singletonList(account2));

        entityManager.merge(account1);
        entityManager.merge(account2);
        entityManager.merge(user1);
        entityManager.merge(user2);
        entityManager.flush();

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

        assertEquals(2, accountRepository.findAll().size());
    }

    @Test
    @Transactional
    @DisplayName("GET /api/v1/accounts/{accountNumber}/balance")
    public void testGetAccountBalance() throws Exception {
        Account account = createAccount("ACC001", new BigDecimal("1000.00"));
        User user = createUser(1, "name-user1", "surname-user1", "123456789", "user1@email.com", Collections.singletonList(account));
        entityManager.merge(account);
        entityManager.merge(user);
        entityManager.flush();

        mockMvc.perform(get("/api/v1/accounts/ACC001/balance")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.balance").value(1000));
    }

    @Test
    @Transactional
    @DisplayName("POST /api/v1/accounts")
    public void testCreateAccount() throws Exception {
        AccountDTO requestAccountDTO = new AccountDTO();
        requestAccountDTO.setNumber("ACC003");
        requestAccountDTO.setBalance(new BigDecimal("3000.00"));
        List<Integer> userIds = Arrays.asList(1, 2);
        requestAccountDTO.setUserIds(userIds);
        requestAccountDTO.setUserIds(userIds);

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
    @DisplayName("GET /api/v1/accounts/{accountNumber}/transactions")
    public void testGetLast5Transactions() throws Exception {
        Account account = createAccount("ACC001", new BigDecimal("1000.00"));
        User user = createUser(1, "name-user1", "surname-user1", "123456789", "user1@email.com", Collections.singletonList(account));
        entityManager.merge(account);
        entityManager.merge(user);
        entityManager.flush();

        Transaction transaction1 = createTransaction(account, Transaction.TransactionType.DEPOSIT, new BigDecimal("100.00"));
        Transaction transaction2 = createTransaction(account, Transaction.TransactionType.WITHDRAW, new BigDecimal("50.00"));
        entityManager.merge(transaction1);
        entityManager.merge(transaction2);
        entityManager.flush();

        mockMvc.perform(get("/api/v1/accounts/ACC001/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].amount").value(100.00))
                .andExpect(jsonPath("$[1].amount").value(50.00));
    }

    @Test
    @Transactional
    @DisplayName("DELETE /api/v1/accounts/{accountNumber}")
    public void testDeleteAccount() throws Exception {
        Account accountToDelete = createAccount("ACC001", new BigDecimal("3000.00"));
        entityManager.merge(accountToDelete);
        entityManager.flush();

        mockMvc.perform(delete("/api/v1/accounts/ACC001", "ACC003"))
                .andExpect(status().isOk());

        assertEquals(0, accountRepository.findAll().size());
    }

    private static Transaction createTransaction(Account account, Transaction.TransactionType type, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setTimestamp(new Date());
        transaction.setCurrency(Transaction.CurrencyType.EURO);
        return transaction;
    }

    private static Account createAccount(String number, BigDecimal balance) {
        Account account = new Account();
        account.setNumber(number);
        account.setBalance(balance);
        account.setUsers(new ArrayList<>());
        return account;
    }

    private static User createUser(int id, String firstName, String lastName, String password, String email, List<Account> accounts) {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setEmail(email);
        user.setAccounts(accounts);
        return user;
    }
}
