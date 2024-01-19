package org.example.backender101homebanking.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class TransactionResponseDTO {
    private List<TransactionDTO> transactions;
}