package org.example.backender101homebanking.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class TransactionResponseDTO {
    private Long id;
    private String accountIban;
    private String type;
    private BigDecimal amount;
    private String currency;
    private Date timestamp;
}