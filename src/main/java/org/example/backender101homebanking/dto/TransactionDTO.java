package org.example.backender101homebanking.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class TransactionDTO {
    private Long id;
    private String type;
    private BigDecimal amount;
    private Date timestamp;
    private String currency;
}
