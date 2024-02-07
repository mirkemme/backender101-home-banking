package org.example.backender101homebanking.mapper;

import org.example.backender101homebanking.dto.UserDTO;
import org.example.backender101homebanking.dto.UserRequestDTO;
import org.example.backender101homebanking.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO convertToDTO(User user);
    UserRequestDTO convertToUserRequestDTO(User user);
    User convertToEntity(UserDTO userDTO);
    @Named("updateUserFromUserDto")
    @Mapping(target = "id", ignore = true)
    void updateUserFromUserDTO(UserDTO dto, @MappingTarget User entity);
}