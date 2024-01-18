package org.example.backender101homebanking.service;

import org.example.backender101homebanking.dto.TransactionRequestDTO;

public interface TransactionService {
    void depositOrWithdraw(TransactionRequestDTO transactionRequestDTO);
}