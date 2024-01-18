package org.example.backender101homebanking.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class BalanceResponseDTO {
    private BigDecimal balance;
}
