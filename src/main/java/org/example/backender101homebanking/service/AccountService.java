package org.example.backender101homebanking.service;

import org.example.backender101homebanking.dto.*;

import java.util.List;

public interface AccountService {
    AccountDTO createAccount(AccountDTO accountDTO);
    List<AccountResponseDTO> getAllAccounts();
    BalanceResponseDTO getAccountBalance(String accountNumber);
    List<TransactionResponseDTO> getLast5Transactions(String accountNumber);
    void deleteAccount(String accountNumber);
}
