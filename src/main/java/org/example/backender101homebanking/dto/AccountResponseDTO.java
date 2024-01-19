package org.example.backender101homebanking.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class AccountResponseDTO {
    private String number;
    private BigDecimal balance;
    private List<UserDTO> users;
}