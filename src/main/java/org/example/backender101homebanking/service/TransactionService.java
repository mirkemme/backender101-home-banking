package org.example.backender101homebanking.service;

import org.example.backender101homebanking.dto.TransactionDTO;

public interface TransactionService {
    TransactionDTO withdraw(TransactionDTO transactionDTO);
    TransactionDTO deposit(TransactionDTO transactionDTO);
}