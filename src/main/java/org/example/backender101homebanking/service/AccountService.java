package org.example.backender101homebanking.service;

import org.example.backender101homebanking.dto.AccountDTO;
import org.example.backender101homebanking.dto.AccountResponseDTO;
import org.example.backender101homebanking.dto.BalanceResponseDTO;
import org.example.backender101homebanking.dto.TransactionDTO;

import java.util.List;

public interface AccountService {
    AccountDTO createAccount(AccountDTO accountDTO);
    List<AccountResponseDTO> getAllAccounts();
    BalanceResponseDTO getAccountBalance(String accountNumber);
    List<TransactionDTO> getAccountTransactions(String accountNumber);
    void deleteAccount(String accountNumber);
}
