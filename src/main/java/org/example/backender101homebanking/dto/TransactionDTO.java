package org.example.backender101homebanking.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class TransactionDTO {
    private Long id;

    @NotBlank(message = "accountNumber is mandatory")
    private String accountNumber;

    @NotBlank(message = "type is mandatory")
    private String type;

    @NotNull(message = "amount is mandatory")
    @Positive(message = "amount must be positive")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

    @NotBlank(message = "currency is mandatory")
    private String currency;
}