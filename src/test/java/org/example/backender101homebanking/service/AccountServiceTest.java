package org.example.backender101homebanking.service;

import org.example.backender101homebanking.dto.AccountDTO;
import org.example.backender101homebanking.dto.AccountResponseDTO;
import org.example.backender101homebanking.dto.BalanceResponseDTO;
import org.example.backender101homebanking.dto.TransactionResponseDTO;
import org.example.backender101homebanking.exception.ResourceNotFoundException;
import org.example.backender101homebanking.mapper.AccountMapper;
import org.example.backender101homebanking.mapper.TransactionMapper;
import org.example.backender101homebanking.mapper.UserMapper;
import org.example.backender101homebanking.model.Account;
import org.example.backender101homebanking.model.Transaction;
import org.example.backender101homebanking.model.User;
import org.example.backender101homebanking.repository.AccountRepository;
import org.example.backender101homebanking.repository.TransactionRepository;
import org.example.backender101homebanking.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.example.backender101homebanking.utils.TestObjectFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountMapper accountMapper;
    @Captor
    private ArgumentCaptor<Account> accountCaptor;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionMapper transactionMapper;
    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    @DisplayName("UnitTest getAllAccounts Success")
    public void testGetAllAccounts() {
        User user = buildUser("name-user1", "surname-user1", "123456789", "user1@email.com");
        Account account1 = buildAccount("ACC001", new BigDecimal("1000.00"), Collections.singletonList(user));
        Account account2 = buildAccount("ACC002", new BigDecimal("2000.00"), Collections.singletonList(user));
        List<Account> accounts = Arrays.asList(account1, account2);

        when(accountRepository.findAll()).thenReturn(accounts);

        List<AccountResponseDTO> result = accountService.getAllAccounts();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("UnitTest getAccountBalance Success")
    public void testGetAccountBalance() {
        User user = buildUser("name-user1", "surname-user1", "123456789", "user1@email.com");
        String accountNumber = "ACC001";
        BigDecimal initialBalance = new BigDecimal("1000.00");
        Account account = buildAccount(accountNumber, initialBalance, Collections.singletonList(user));

        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(account));

        BalanceResponseDTO result = accountService.getAccountBalance(accountNumber);

        assertNotNull(result);
        assertEquals(initialBalance, result.getBalance());
        verify(accountRepository, times(1)).findById(accountNumber);
    }

    @Test
    @DisplayName("UnitTest createAccount Success")
    public void testCreateAccount() {
        User user = buildUser("name-user1", "surname-user1", "123456789", "user1@email.com");
        List<User> users = Collections.singletonList(user);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setNumber("ACC001");
        accountDTO.setBalance(new BigDecimal("1000.00"));
        accountDTO.setUserIds(Arrays.asList(0));

        Account account = buildAccount("ACC001", new BigDecimal("1000.00"), users);

        when(userRepository.findAllById(accountDTO.getUserIds())).thenReturn(users);
        when(accountMapper.convertToEntity(accountDTO)).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountDTO result = accountService.createAccount(accountDTO);

        verify(accountRepository, times(1)).save(accountCaptor.capture());
        Account capturedAccount = accountCaptor.getValue();

        assertNotNull(result);
        assertEquals(users, capturedAccount.getUsers());
    }

    @Test
    @DisplayName("UnitTest deleteAccount Success")
    public void testDeleteAccountSuccess() {
        Account account = buildAccount("ACC001", new BigDecimal("1000.00"), Collections.emptyList());

        when(accountRepository.findById("ACC001")).thenReturn(Optional.of(account));

        assertDoesNotThrow(() -> accountService.deleteAccount("ACC001"));

        verify(accountRepository, times(1)).findById("ACC001");
        verify(accountRepository, times(1)).delete(account);
    }

    @Test
    @DisplayName("UnitTest getLast5Transactions Success")
    public void testGetLast5Transactions_Success() {
        String accountNumber = "ACC001";
        Account account = buildAccount(accountNumber, new BigDecimal("1000.00"), Collections.emptyList());
        List<Transaction> transactions = Arrays.asList(
                buildTransaction(account, new BigDecimal("1000.0"), Transaction.CurrencyType.EURO, Transaction.TransactionType.DEPOSIT),
                buildTransaction(account, new BigDecimal("500.0"), Transaction.CurrencyType.EURO, Transaction.TransactionType.WITHDRAW),
                buildTransaction(account, new BigDecimal("2000.0"), Transaction.CurrencyType.EURO, Transaction.TransactionType.DEPOSIT)
        );

        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(account));
        when(transactionRepository.findAllByAccountNumberOrderByTimestampDesc(accountNumber)).thenReturn(transactions);
        when(transactionMapper.convertToResponseDto(any(Transaction.class))).thenAnswer(
                invocation -> {
                    Transaction inputTransaction = invocation.getArgument(0);
                    TransactionResponseDTO responseDTO = new TransactionResponseDTO();
                    responseDTO.setAccountNumber(inputTransaction.getAccount().getNumber());
                    responseDTO.setType(inputTransaction.getType().toString());
                    responseDTO.setCurrency(inputTransaction.getCurrency().toString());
                    return responseDTO;
                });

        List<TransactionResponseDTO> result = accountService.getLast5Transactions(accountNumber);

        verify(accountRepository, times(1)).findById(accountNumber);
        verify(transactionRepository, times(1)).findAllByAccountNumberOrderByTimestampDesc(accountNumber);
        verify(transactionMapper, times(transactions.size())).convertToResponseDto(any(Transaction.class));

        assertNotNull(result);
        assertEquals(transactions.size(), result.size());
    }

    /********** FAILURE TESTS **********/

    @Test
    @DisplayName("UnitTest getAccountBalance AccountNotFound")
    public void testGetAccountBalanceAccountNotFound() {
        String notExistingAccountNumber = "NOT_EXISTING_ACCOUNT";

        when(accountRepository.findById(notExistingAccountNumber)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> accountService.getAccountBalance(notExistingAccountNumber));

        verify(accountRepository, times(1)).findById(notExistingAccountNumber);
    }

    @Test
    @DisplayName("UnitTest createAccount Failure - Invalid User Ids")
    public void testCreateAccountFailure_InvalidUserIds() {
        User user = buildUser("name-user1", "surname-user1", "123456789", "user1@email.com");
        List<User> users = Collections.singletonList(user);
        int notExistingUserId = 1;

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setNumber("ACC001");
        accountDTO.setBalance(new BigDecimal("1000.00"));
        accountDTO.setUserIds(Arrays.asList(0, notExistingUserId));

        when(userRepository.findAllById(accountDTO.getUserIds())).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> accountService.createAccount(accountDTO));

        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("UnitTest deleteAccount Failure - Account not found")
    public void testDeleteAccountFailureAccountNotFound() {
        String accountNumber = "ACC001";

        when(accountRepository.findById(accountNumber)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> accountService.deleteAccount(accountNumber));

        assertEquals("Account not found with account number: " + accountNumber, exception.getMessage());

        verify(accountRepository, times(1)).findById(accountNumber);
        verify(accountRepository, never()).delete(any());
    }

    @Test
    @DisplayName("UnitTest getLast5Transactions Failure - Account not found")
    public void testGetLast5Transactions_AccountNotFound() {
        String nonExistingAccountNumber = "NOT EXISTING ACCOUNT NUMBER";

        when(accountRepository.findById(nonExistingAccountNumber)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.getLast5Transactions(nonExistingAccountNumber));

        verify(accountRepository, times(1)).findById(nonExistingAccountNumber);
        verify(transactionRepository, never()).findAllByAccountNumberOrderByTimestampDesc(anyString());
        verify(transactionMapper, never()).convertToResponseDto(any(Transaction.class));
    }
}
