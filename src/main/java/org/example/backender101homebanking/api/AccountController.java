package org.example.backender101homebanking.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backender101homebanking.dto.*;
import org.example.backender101homebanking.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    @GetMapping("/all")
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts() {
        List<AccountResponseDTO> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<BalanceResponseDTO> getAccountBalance(@PathVariable String accountNumber) {
        BalanceResponseDTO balanceResponseDTO = accountService.getAccountBalance(accountNumber);

        return new ResponseEntity<>(balanceResponseDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createAccount(@Valid @RequestBody AccountDTO accountDTO) {
        String savedAccountNumber = accountService.createAccount(accountDTO);
        return new ResponseEntity<>(savedAccountNumber, HttpStatus.CREATED);
    }

    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<List<TransactionResponseDTO>> getLast5Transactions(@PathVariable String accountNumber) {
        List<TransactionResponseDTO> transactions = accountService.getLast5Transactions(accountNumber);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
