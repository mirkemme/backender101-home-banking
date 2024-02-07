package org.example.backender101homebanking.utils;

import org.example.backender101homebanking.dto.TransactionDTO;
import org.example.backender101homebanking.dto.TransactionResponseDTO;
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
    public static Transaction buildTransaction(Account account, BigDecimal amount,  Transaction.CurrencyType currency, Transaction.TransactionType type) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setType(type);
        transaction.setTimestamp(new Date());

        return transaction;
    }

    public static Account buildAccount(String number, BigDecimal balance, List<User> users) {
        Account account = new Account();
        account.setNumber(number);
        account.setBalance(balance);
        account.setUsers(users);
        account.setTransactions(new ArrayList<>());

        return account;
    }

    public static User buildUser(String firstName, String lastName, String password, String email) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setEmail(email);
        user.setAccounts(new ArrayList<>());

        return user;
    }

    public static UserDTO buildUserDTO(String firstName, String lastName, String password, String email) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setPassword(password);
        userDTO.setEmail(email);
        userDTO.setAccounts(new ArrayList<>());

        return userDTO;
    }

    public static TransactionDTO buildTransactionDTO(String accountNumber, String type, BigDecimal amount, String
                                                     currency) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountNumber(accountNumber);
        transactionDTO.setType(type);
        transactionDTO.setAmount(amount);
        transactionDTO.setCurrency(currency);

        return transactionDTO;
    }
    public static TransactionResponseDTO buildTransactionResponseDTO(String accountNumber, String type, String currency) {
        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setAccountNumber(accountNumber);
        responseDTO.setType(type);
        responseDTO.setCurrency(currency);
        return responseDTO;
    }

}
