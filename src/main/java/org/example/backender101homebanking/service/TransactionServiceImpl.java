package org.example.backender101homebanking.service;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.backender101homebanking.dto.TransactionDTO;
import org.example.backender101homebanking.exception.InsufficientBalanceException;
import org.example.backender101homebanking.mapper.TransactionMapper;
import org.example.backender101homebanking.model.Account;
import org.example.backender101homebanking.model.Transaction;
import org.example.backender101homebanking.repository.AccountRepository;
import org.example.backender101homebanking.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService  {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    public TransactionDTO withdraw(TransactionDTO transactionDTO) {
        validateTransactionDTO(transactionDTO);

        Account account = getAccount(transactionDTO.getAccountNumber());
        BigDecimal currentBalance = account.getBalance();
        BigDecimal withdrawalAmount = transactionDTO.getAmount();

        if (currentBalance.compareTo(withdrawalAmount) < 0) {
            throw new InsufficientBalanceException("Insufficient funds for withdrawal.");
        }

        BigDecimal newBalance = currentBalance.subtract(withdrawalAmount);
        account.setBalance(newBalance);

        Transaction transaction = transactionMapper.convertToEntity(transactionDTO);
        transaction.setAccount(account);
        transaction.setAmount(withdrawalAmount.negate()); // l'importo del prelievo è negativo
        transaction.setTimestamp(new Date());

        transactionRepository.save(transaction);
        transactionDTO.setId(transaction.getId());
        transactionDTO.setAmount(withdrawalAmount.negate());

        return transactionDTO;
    }

    public TransactionDTO deposit(TransactionDTO transactionDTO) {
        validateTransactionDTO(transactionDTO);

        Account account = getAccount(transactionDTO.getAccountNumber());

        BigDecimal currentBalance = account.getBalance();
        BigDecimal withdrawalAmount = transactionDTO.getAmount();

        // Esegui il versamento
        BigDecimal newBalance = currentBalance.add(withdrawalAmount);
        account.setBalance(newBalance);

        Transaction transaction = transactionMapper.convertToEntity(transactionDTO);
        transaction.setAccount(account);
        transaction.setAmount(withdrawalAmount);
        transaction.setTimestamp(new Date());

        transactionRepository.save(transaction);
        transactionDTO.setId(transaction.getId());

        return transactionDTO;
    }

    private void validateTransactionDTO(TransactionDTO transactionDTO) {
        if (StringUtils.isBlank(transactionDTO.getAccountNumber())) {
            throw new IllegalArgumentException("Account number is mandatory");
        }

        if (transactionDTO.getAmount() == null || transactionDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be a positive value");
        }

        if (StringUtils.isBlank(transactionDTO.getType())) {
            throw new IllegalArgumentException("Transaction type is mandatory");
        }
        
        if (StringUtils.isBlank(transactionDTO.getCurrency())) {
            throw new IllegalArgumentException("Currency is mandatory");
        }
    }

    private Account getAccount(String accountNumber) {
        return accountRepository.findById(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }
}
