package org.example.backender101homebanking.service;

import org.example.backender101homebanking.dto.AccountDTO;
import org.example.backender101homebanking.dto.AccountResponseDTO;
import org.example.backender101homebanking.dto.BalanceResponseDTO;
import org.example.backender101homebanking.dto.TransactionResponseDTO;

import java.util.List;

public interface AccountService {
    String createAccount(AccountDTO accountDTO);

    List<AccountResponseDTO> getAllAccounts();

    BalanceResponseDTO getAccountBalance(String accountIban);

    void deleteAccount(String accountIban);

    List<TransactionResponseDTO> getLast5Transactions(String accountIban);
}
