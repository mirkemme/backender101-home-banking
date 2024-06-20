package org.example.backender101homebanking.service;

import org.example.backender101homebanking.dto.UserDTO;
import org.example.backender101homebanking.dto.UserUpdateDto;
import org.example.backender101homebanking.model.User;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(long userId);

    void addUser(User user);

    UserDTO updateUser(long userId, UserUpdateDto userUpdateDto);

    void deleteUser(long userId);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
