package org.example.backender101homebanking.service;

import org.example.backender101homebanking.dto.UserDTO;
import org.example.backender101homebanking.exception.ResourceNotFoundException;
import org.example.backender101homebanking.mapper.ManualUserMapper;
import org.example.backender101homebanking.model.User;
import org.example.backender101homebanking.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ManualUserMapper manualUserMapper;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Test getAllUsers Success")
    public void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));

        when(manualUserMapper.convertToDTO(any(User.class))).thenReturn(new UserDTO());

        List<UserDTO> result = userService.getAllUsers();

        // Verifying that the expected methods were called
        verify(userRepository, times(1)).findAll();
        verify(manualUserMapper, times(2)).convertToDTO(any(User.class));

        // Verifying the expected result
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Test findById Success")
    public void testGetUserById() {
        int userId = 1;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDTO userDTO = new UserDTO();
        when(manualUserMapper.convertToDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(userId);

        assertEquals(userDTO, result);
    }

    @Test
    @DisplayName("Test findById not found")
    void testFindByIdNotFound(){
        int userId = 2;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    @DisplayName("Test addUser Success")
    public void testAddUser() {
        UserDTO userDTO = new UserDTO();
        User user = new User();
        when(manualUserMapper.convertToEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        UserDTO result = userService.addUser(userDTO);

        verify(manualUserMapper, times(1)).convertToEntity(userDTO);
        verify(userRepository, times(1)).save(user);
        verify(manualUserMapper, times(1)).convertToDTO(user);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Test updateUser Success")
    public void testUpdateUser() {
        int userId = 1;
        UserDTO userDTO = new UserDTO();
        User existingUser = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        UserDTO result = userService.updateUser(userId, userDTO);

        verify(userRepository, times(1)).findById(userId);
        verify(manualUserMapper, times(1)).updateUserFromDTO(existingUser, userDTO);
        verify(userRepository, times(1)).save(existingUser);
        verify(manualUserMapper, times(1)).convertToDTO(existingUser);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Test deleteUser Success")
    public void testDeleteUser() {
        int userId = 1;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    @DisplayName("Test deleteUser Not Found")
    public void testDeleteUserNotFound() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(userId));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).delete(any(User.class));
    }
}
