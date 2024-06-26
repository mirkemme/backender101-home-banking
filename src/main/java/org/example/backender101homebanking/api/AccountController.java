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

    @GetMapping("/{accountIban}/balance")
    public ResponseEntity<BalanceResponseDTO> getAccountBalance(@PathVariable String accountIban) {
        BalanceResponseDTO balanceResponseDTO = accountService.getAccountBalance(accountIban);

        return new ResponseEntity<>(balanceResponseDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createAccount(@Valid @RequestBody AccountDTO accountDTO) {
        String savedAccountIban = accountService.createAccount(accountDTO);
        return new ResponseEntity<>(savedAccountIban, HttpStatus.CREATED);
    }

    @GetMapping("/{accountIban}/transactions")
    public ResponseEntity<List<TransactionResponseDTO>> getLast5Transactions(@PathVariable String accountIban) {
        List<TransactionResponseDTO> transactions = accountService.getLast5Transactions(accountIban);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @DeleteMapping("/{accountIban}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountIban) {
        accountService.deleteAccount(accountIban);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
