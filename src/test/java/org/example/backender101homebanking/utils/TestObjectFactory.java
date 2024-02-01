package org.example.backender101homebanking.utils;

import jakarta.persistence.EntityManager;
import org.example.backender101homebanking.dto.UserDTO;
import org.example.backender101homebanking.model.Account;
import org.example.backender101homebanking.model.Transaction;
import org.example.backender101homebanking.model.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class TestObjectFactory {
    private static EntityManager entityManager;

    public TestObjectFactory(EntityManager entityManager) {
        TestObjectFactory.entityManager = entityManager;
    }
    public static Transaction buildTransaction(Account account, Transaction.TransactionType type, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setTimestamp(new Date());
        transaction.setCurrency(Transaction.CurrencyType.EURO);
        mergeAndFlush(transaction);

        return transaction;
    }

    public static Account buildAccount(String number, BigDecimal balance) {
        Account account = new Account();
        account.setNumber(number);
        account.setBalance(balance);
        account.setUsers(new ArrayList<>());
        mergeAndFlush(account);

        return account;
    }

    public static User buildUser(String firstName, String lastName, String password, String email, List<Account> accounts) {
        User user = new User();
        //user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setEmail(email);
        user.setAccounts(accounts);
        mergeAndFlush(user);

        return user;
    }

    public static UserDTO buildUserDTO(String firstName, String lastName, String password, String email) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setPassword(password);
        userDTO.setEmail(email);

        return userDTO;
    }

    public static <T> void mergeAndFlush(T entity) {
        entityManager.merge(entity);
        entityManager.flush();
    }
}
