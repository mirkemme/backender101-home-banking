package org.example.backender101homebanking.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class AccountRequestDTO {
    private String number;
    private BigDecimal balance;
}
