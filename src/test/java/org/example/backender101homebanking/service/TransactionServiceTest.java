package org.example.backender101homebanking.service;

import org.example.backender101homebanking.dto.TransactionDTO;
import org.example.backender101homebanking.exception.InsufficientBalanceException;
import org.example.backender101homebanking.exception.ResourceNotFoundException;
import org.example.backender101homebanking.mapper.TransactionMapper;
import org.example.backender101homebanking.model.Account;
import org.example.backender101homebanking.model.Transaction;
import org.example.backender101homebanking.repository.AccountRepository;
import org.example.backender101homebanking.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionMapper transactionMapper;
    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    @DisplayName("UnitTest Withdraw Success")
    public void testWithdrawSuccess() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountIban("IT60X0542811101000000654321");
        transactionDTO.setAmount(BigDecimal.valueOf(50));

        Account account = new Account();
        account.setIban("IT60X0542811101000000654321");
        account.setBalance(BigDecimal.valueOf(100));

        Transaction transaction = new Transaction();

        when(accountRepository.findById("IT60X0542811101000000654321")).thenReturn(Optional.of(account));
        when(transactionMapper.convertToEntity(transactionDTO)).thenReturn(transaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionDTO result = transactionService.withdraw(transactionDTO);

        assertNotNull(result);
        assertEquals(transactionDTO.getAccountIban(), result.getAccountIban());
        assertEquals(transactionDTO.getAmount().negate(), result.getAmount().negate());
    }

    @Test
    @DisplayName("UnitTest Deposit Success")
    public void testDepositSuccess() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountIban("IT60X0542811101000000654321");
        transactionDTO.setAmount(BigDecimal.valueOf(50));

        Account account = new Account();
        account.setIban("IT60X0542811101000000654321");
        account.setBalance(BigDecimal.valueOf(100));

        Transaction transaction = new Transaction();

        when(accountRepository.findById("IT60X0542811101000000654321")).thenReturn(Optional.of(account));
        when(transactionMapper.convertToEntity(transactionDTO)).thenReturn(transaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionDTO result = transactionService.deposit(transactionDTO);

        assertNotNull(result);
        assertEquals(transactionDTO.getAccountIban(), result.getAccountIban());
        assertEquals(transactionDTO.getAmount(), result.getAmount());
    }

    /********** FAILURE TESTS **********/

    @Test
    @DisplayName("UnitTest Deposit Failure: Insufficient Balance")
    public void testWithdrawInsufficientBalance() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountIban("IT60X0542811101000000654321");
        transactionDTO.setAmount(BigDecimal.valueOf(150));

        Account account = new Account();
        account.setIban("IT60X0542811101000000654321");
        account.setBalance(BigDecimal.valueOf(100));

        when(accountRepository.findById("IT60X0542811101000000654321")).thenReturn(Optional.of(account));

        assertThrows(InsufficientBalanceException.class, () -> {
            transactionService.withdraw(transactionDTO);
        });
    }

    @Test
    @DisplayName("UnitTest Deposit Failure: Account not found")
    public void testAccountNotFound() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountIban("IT60X0542811101000000654321");
        transactionDTO.setAmount(BigDecimal.valueOf(50));

        when(accountRepository.findById("IT60X0542811101000000654321")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.withdraw(transactionDTO);
        });
    }
}
