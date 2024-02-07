package org.example.backender101homebanking.service;

import org.example.backender101homebanking.dto.TransactionDTO;
import org.example.backender101homebanking.dto.TransactionResponseDTO;
import org.example.backender101homebanking.model.Account;

import java.util.List;

public interface TransactionService {
    TransactionDTO withdraw(TransactionDTO transactionDTO);
    TransactionDTO deposit(TransactionDTO transactionDTO);
    List<TransactionResponseDTO> getLast5Transactions(Account account);
}