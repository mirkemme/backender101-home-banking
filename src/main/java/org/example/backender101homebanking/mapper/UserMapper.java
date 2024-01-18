package org.example.backender101homebanking.mapper;

import org.example.backender101homebanking.dto.UserDTO;
import org.example.backender101homebanking.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDTO convertToDto(User user);
    User convertToEntity(UserDTO userDTO);
}