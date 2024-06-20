package org.example.backender101homebanking.service;

import lombok.RequiredArgsConstructor;
import org.example.backender101homebanking.dto.AccountRequestDTO;
import org.example.backender101homebanking.dto.UserDTO;
import org.example.backender101homebanking.dto.UserUpdateDto;
import org.example.backender101homebanking.exception.ResourceNotFoundException;
import org.example.backender101homebanking.mapper.AccountMapper;
import org.example.backender101homebanking.mapper.UserMapper;
import org.example.backender101homebanking.model.Account;
import org.example.backender101homebanking.model.User;
import org.example.backender101homebanking.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> {
                    UserDTO userDTO = userMapper.convertToDTO(user);
                    List<Account> accounts = user.getAccounts();
                    List<AccountRequestDTO> accountRequestDTOSDTOs = accounts.stream()
                            .map(accountMapper::convertToAccountRequestDTO)
                            .collect(Collectors.toList());
                    userDTO.setAccounts(accountRequestDTOSDTOs);
                    return userDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return userMapper.convertToDTO(user);
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public UserDTO updateUser(long userId, UserUpdateDto userUpdateDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        } else {
            userUpdateDto.setPassword(existingUser.getPassword());
        }

        userMapper.updateUserFromUserUpdateDto(userUpdateDto, existingUser);

        User updatedUser = userRepository.save(existingUser);

        return userMapper.convertToDTO(updatedUser);
    }

    @Override
    public void deleteUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        userRepository.delete(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
