package org.example.backender101homebanking.mapper;

import org.example.backender101homebanking.dto.UserDTO;
import org.example.backender101homebanking.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements EntityMapper<UserDTO, User> {

    @Override
    public User convertToEntity(UserDTO dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());

        return user;
    }

    @Override
    public UserDTO convertToDTO(User entity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(entity.getId());
        userDTO.setFirstName(entity.getFirstName());
        userDTO.setLastName(entity.getLastName());
        userDTO.setPassword(entity.getPassword());
        userDTO.setEmail(entity.getEmail());

        return userDTO;
    }

    // Update the existing user with data from userDTO
    public void updateUserFromDTO(User user, UserDTO userDTO) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
    }
}
