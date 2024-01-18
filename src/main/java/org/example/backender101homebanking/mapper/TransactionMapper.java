package org.example.backender101homebanking.mapper;

import org.mapstruct.Mapper;

@Mapper
public interface TransactionMapper {

    /*TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);
    TransactionDTO toDto(Transaction transaction);
    TransactionResponseDTO toTransactionResponseDto(List<TransactionDTO> transactions);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "account", ignore = true)
    Transaction toEntity(TransactionRequestDTO transactionRequestDTO, Account account);*/
}