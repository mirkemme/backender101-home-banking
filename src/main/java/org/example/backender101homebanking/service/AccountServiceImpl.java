package org.example.backender101homebanking.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.backender101homebanking.dto.*;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.backender101homebanking.utils.UserIdValidator.validateUserIds;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AccountMapper accountMapper;
    private final UserMapper userMapper;
    private final TransactionMapper transactionMapper;

    @Override
    public List<AccountResponseDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(account -> {
                    AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
                    accountResponseDTO.setNumber(account.getNumber());
                    accountResponseDTO.setBalance(account.getBalance());

                    List<UserDTO> users = account.getUsers().stream()
                            .map(userMapper::convertToDto)
                            .collect(Collectors.toList());

                    accountResponseDTO.setUsers(users);

                    return accountResponseDTO;
                })
                .collect(Collectors.toList());
    }

    public BalanceResponseDTO getAccountBalance(String accountNumber) {
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        BalanceResponseDTO balanceResponseDTO = new BalanceResponseDTO(account.getBalance());
        return balanceResponseDTO;
    }

    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) {
        List<User> users = userRepository.findAllById(accountDTO.getUserIds());
        validateUserIds(accountDTO.getUserIds(), users);
        Account account = accountMapper.convertToEntity(accountDTO);
        account.setUsers(users);
        Account savedAccount = accountRepository.save(account);

        return accountDTO;
    }

    @Override
    public List<TransactionResponseDTO> getLast5Transactions(String accountNumber) {
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        List<Transaction> allTransactions = transactionRepository.findAllByAccountNumberOrderByTimestampDesc(account.getNumber());
        List<Transaction> last5Transactions = allTransactions.subList(0, Math.min(allTransactions.size(), 5));

        List<TransactionResponseDTO> transactionResponseDTOs = last5Transactions.stream()
                .map(transactionMapper::convertToResponseDto)
                .collect(Collectors.toList());

        return transactionResponseDTOs;
    }

    @Override
    public void deleteAccount(String accountNumber) {
        Account account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with account number: " + accountNumber));
        accountRepository.delete(account);
    }
}