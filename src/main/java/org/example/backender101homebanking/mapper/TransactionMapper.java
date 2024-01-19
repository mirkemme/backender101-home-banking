package org.example.backender101homebanking.mapper;

import org.example.backender101homebanking.dto.TransactionDTO;
import org.example.backender101homebanking.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(target = "accountNumber", ignore = true)
    TransactionDTO convertToDto(Transaction transaction);
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    Transaction convertToEntity(TransactionDTO transactionDTO);
}