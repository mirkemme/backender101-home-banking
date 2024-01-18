package org.example.backender101homebanking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class TransactionRequestDTO {
    @NotBlank(message = "accountNumber is mandatory")
    private String accountNumber;
    @NotBlank(message = "type is mandatory")
    private String type;
    @NotBlank(message = "amount is mandatory")
    private BigDecimal amount;
    @NotBlank(message = "currency is mandatory")
    private String currency;
}