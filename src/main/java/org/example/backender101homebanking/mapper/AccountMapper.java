package org.example.backender101homebanking.mapper;

import org.example.backender101homebanking.dto.AccountDTO;
import org.example.backender101homebanking.dto.AccountRequestDTO;
import org.example.backender101homebanking.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "number", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "users", ignore = true)
    Account convertToEntity(AccountDTO accountDTO);

    AccountRequestDTO convertToAccountRequestDTO(Account account);
}
