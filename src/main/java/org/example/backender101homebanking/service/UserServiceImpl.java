package org.example.backender101homebanking.service;

import lombok.RequiredArgsConstructor;
import org.example.backender101homebanking.dto.UserDTO;
import org.example.backender101homebanking.exception.ResourceNotFoundException;
import org.example.backender101homebanking.mapper.ManualUserMapper;
import org.example.backender101homebanking.model.User;
import org.example.backender101homebanking.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ManualUserMapper manualUserMapper;

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(manualUserMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return manualUserMapper.convertToDTO(user);
    }

    @Override
    public UserDTO addUser(UserDTO userDTO) {
        User user = manualUserMapper.convertToEntity(userDTO);
        User savedUser = userRepository.save(user);

        return manualUserMapper.convertToDTO(savedUser);
    }

    @Override
    public UserDTO updateUser(int userId, UserDTO userDTO) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        manualUserMapper.updateUserFromDTO(existingUser, userDTO);
        User updatedUser = userRepository.save(existingUser);

        return manualUserMapper.convertToDTO(updatedUser);
    }

    @Override
    public void deleteUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        userRepository.delete(user);
    }
}
