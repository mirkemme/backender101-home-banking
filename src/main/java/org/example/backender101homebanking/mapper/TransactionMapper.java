package org.example.backender101homebanking.mapper;

import org.example.backender101homebanking.dto.TransactionDTO;
import org.example.backender101homebanking.dto.TransactionResponseDTO;
import org.example.backender101homebanking.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(target = "accountNumber", source = "transaction.account.number")
    @Mapping(source = "type", target = "type", qualifiedByName = "transactionTypeToString")
    @Mapping(source = "currency", target = "currency", qualifiedByName = "currencyTypeToString")
    TransactionResponseDTO convertToResponseDto(Transaction transaction);

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(source = "type", target = "type", qualifiedByName = "stringToTransactionType")
    @Mapping(source = "currency", target = "currency", qualifiedByName = "stringToCurrencyType")
    Transaction convertToEntity(TransactionDTO transactionDTO);

    @Named("transactionTypeToString")
    default String transactionTypeToString(Transaction.TransactionType type) {
        return type.toString();
    }

    @Named("currencyTypeToString")
    default String currencyTypeToString(Transaction.CurrencyType currency) {
        return currency.toString();
    }

    @Named("stringToTransactionType")
    default Transaction.TransactionType stringToTransactionType(String type) {
        return Transaction.TransactionType.fromString(type);
    }

    @Named("stringToCurrencyType")
    default Transaction.CurrencyType stringToCurrencyType(String currency) {
        return Transaction.CurrencyType.fromString(currency);
    }
}