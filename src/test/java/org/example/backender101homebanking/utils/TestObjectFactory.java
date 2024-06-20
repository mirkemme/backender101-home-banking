package org.example.backender101homebanking.utils;

import org.example.backender101homebanking.dto.TransactionDTO;
import org.example.backender101homebanking.dto.TransactionResponseDTO;
import org.example.backender101homebanking.model.Account;
import org.example.backender101homebanking.model.Transaction;
import org.example.backender101homebanking.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Component
public class TestObjectFactory {

    private static PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        TestObjectFactory.passwordEncoder = passwordEncoder;
    }

    public static Transaction buildTransaction(Account account, BigDecimal amount, Transaction.CurrencyType currency, Transaction.TransactionType type) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setType(type);
        transaction.setTimestamp(new Date());

        return transaction;
    }

    public static Account buildAccount(String iban, BigDecimal balance, List<User> users) {
        Account account = new Account();
        account.setIban(iban);
        account.setBalance(balance);
        account.setUsers(users);
        account.setTransactions(new ArrayList<>());

        return account;
    }

    public static User buildUser(String firstName,
                                 String lastName,
                                 String username,
                                 String email,
                                 String password
                                 ) {

        //PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(new HashSet<>());
        user.setAccounts(new ArrayList<>());
        user.setEnabled(true);

        return user;
    }

    public static TransactionDTO buildTransactionDTO(String accountIban, String type, BigDecimal amount, String
                                                     currency) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountIban(accountIban);
        transactionDTO.setType(type);
        transactionDTO.setAmount(amount);
        transactionDTO.setCurrency(currency);

        return transactionDTO;
    }
    public static TransactionResponseDTO buildTransactionResponseDTO(String accountIban, String type, String currency) {
        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setAccountIban(accountIban);
        responseDTO.setType(type);
        responseDTO.setCurrency(currency);
        return responseDTO;
    }

}
