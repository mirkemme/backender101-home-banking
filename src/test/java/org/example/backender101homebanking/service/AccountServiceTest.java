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
import org.example.backender101homebanking.utils.IbanGenerator;
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
    @Mock
    private TransactionService transactionService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private IbanGenerator ibanGenerator;
    @InjectMocks
    private AccountServiceImpl accountService;
    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    @Test
    @DisplayName("UnitTest getAllAccounts Success")
    public void testGetAllAccounts() {
        User user = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");
        Account account1 = buildAccount("IT60X0542811101000000654321", new BigDecimal("1000.00"), Collections.singletonList(user));
        Account account2 = buildAccount("IT60X0542811101000000654322", new BigDecimal("2000.00"), Collections.singletonList(user));
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
        User user = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");
        String accountNumber = "IT60X0542811101000000654321";
        Account account = buildAccount(accountNumber, new BigDecimal("1000.00"), Collections.singletonList(user));

        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(account));

        BalanceResponseDTO result = accountService.getAccountBalance(accountNumber);

        assertNotNull(result);
        assertEquals(new BigDecimal("1000.00"), result.getBalance());
        verify(accountRepository, times(1)).findById(accountNumber);
    }

    @Test
    @DisplayName("UnitTest createAccount Success")
    public void testCreateAccount() {
        User user = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");

        List<User> users = Collections.singletonList(user);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setBalance(new BigDecimal("1000.00"));
        accountDTO.setUserIds(Arrays.asList(0L));

        Account account = new Account();
        account.setBalance(new BigDecimal("1000.00"));
        account.setUsers(users);

        when(userRepository.findAllById(accountDTO.getUserIds())).thenReturn(users);
        when(accountMapper.convertToEntity(accountDTO)).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(ibanGenerator.generateIban()).thenReturn("IT60X0542811101000000654321");

        String result = accountService.createAccount(accountDTO);
        String iban = ibanGenerator.generateIban();

        verify(accountRepository, times(1)).save(accountCaptor.capture());

        Account capturedAccount = accountCaptor.getValue();

        assertNotNull(result);
        assertEquals(users, capturedAccount.getUsers());
        assertEquals(iban, "IT60X0542811101000000654321");
    }

    @Test
    @DisplayName("UnitTest deleteAccount Success")
    public void testDeleteAccountSuccess() {
        Account account = buildAccount("IT60X0542811101000000654321", new BigDecimal("1000.00"), Collections.emptyList());

        when(accountRepository.findById("IT60X0542811101000000654321")).thenReturn(Optional.of(account));

        assertDoesNotThrow(() -> accountService.deleteAccount("IT60X0542811101000000654321"));

        verify(accountRepository, times(1)).findById("IT60X0542811101000000654321");
        verify(accountRepository, times(1)).delete(account);
    }

    @Test
    @DisplayName("UnitTest getLast5Transactions Success")
    public void testGetLast5Transactions_Success() {
        String accountNumber = "IT60X0542811101000000654321";
        Account account = buildAccount(accountNumber, new BigDecimal("1000.00"), Collections.emptyList());
        List<Transaction> transactions = Arrays.asList(
                buildTransaction(account, new BigDecimal("1000.0"), Transaction.CurrencyType.EURO, Transaction.TransactionType.DEPOSIT),
                buildTransaction(account, new BigDecimal("500.0"), Transaction.CurrencyType.EURO, Transaction.TransactionType.WITHDRAW),
                buildTransaction(account, new BigDecimal("2000.0"), Transaction.CurrencyType.EURO, Transaction.TransactionType.DEPOSIT)
        );
        List<TransactionResponseDTO> transactionResponseDTOs = Arrays.asList(
                buildTransactionResponseDTO(accountNumber, "DEPOSIT", "EURO"),
                buildTransactionResponseDTO(accountNumber, "WITHDRAW", "EURO"),
                buildTransactionResponseDTO(accountNumber, "DEPOSIT", "EURO")
        );

        when(accountRepository.findById(accountNumber)).thenReturn(Optional.of(account));
        when(transactionService.getLast5Transactions(account)).thenReturn(transactionResponseDTOs);

        List<TransactionResponseDTO> result = accountService.getLast5Transactions(accountNumber);

        verify(accountRepository, times(1)).findById(accountNumber);
        verify(transactionService, times(1)).getLast5Transactions(account);

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
        User user = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");
        List<User> users = Collections.singletonList(user);
        Long notExistingUserId = 1L;

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setBalance(new BigDecimal("1000.00"));
        accountDTO.setUserIds(Arrays.asList(0L, notExistingUserId));

        when(userRepository.findAllById(accountDTO.getUserIds())).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> accountService.createAccount(accountDTO));

        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("UnitTest deleteAccount Failure - Account not found")
    public void testDeleteAccountFailureAccountNotFound() {
        String accountNumber = "IT60X0542811101000000654321";

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
        String nonExistingAccountNumber = "IT60X0542811101000000654320";

        when(accountRepository.findById(nonExistingAccountNumber)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.getLast5Transactions(nonExistingAccountNumber));

        verify(accountRepository, times(1)).findById(nonExistingAccountNumber);
        verify(transactionRepository, never()).findAllByAccountNumberOrderByTimestampDesc(anyString());
        verify(transactionMapper, never()).convertToResponseDto(any(Transaction.class));
    }
}
