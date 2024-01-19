package org.example.backender101homebanking.mapper;

import org.example.backender101homebanking.dto.UserDTO;
import org.example.backender101homebanking.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO convertToDto(User user);
    User convertToEntity(UserDTO userDTO);
}