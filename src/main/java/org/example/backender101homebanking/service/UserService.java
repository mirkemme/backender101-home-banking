package org.example.backender101homebanking.service;

import org.example.backender101homebanking.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();

    UserDTO getUserById(int userId);
    UserDTO addUser(UserDTO userDTO);
    UserDTO updateUser(int userId, UserDTO userDTO);
    void deleteUser(int userId);
}
