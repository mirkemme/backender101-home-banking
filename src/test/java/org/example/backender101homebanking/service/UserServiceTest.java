package org.example.backender101homebanking.service;

import org.example.backender101homebanking.dto.UserDTO;
import org.example.backender101homebanking.exception.ResourceNotFoundException;
import org.example.backender101homebanking.mapper.UserMapper;
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
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;
    @Test
    @DisplayName("UnitTest getAllUsers Success")
    public void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));
        when(userMapper.convertToDTO(any(User.class))).thenReturn(new UserDTO());

        List<UserDTO> result = userService.getAllUsers();

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(2)).convertToDTO(any(User.class));

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("UnitTest findById Success")
    public void testGetUserById() {
        int userId = 1;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDTO userDTO = new UserDTO();
        when(userMapper.convertToDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(userId);

        assertEquals(userDTO, result);
    }

    @Test
    @DisplayName("UnitTest findById not found")
    void testFindByIdNotFound(){
        int userId = 2;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    @DisplayName("UnitTest addUser Success")
    public void testAddUser() {
        UserDTO userDTO = new UserDTO();
        User user = new User();
        when(userMapper.convertToEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        UserDTO result = userService.addUser(userDTO);

        verify(userMapper, times(1)).convertToEntity(userDTO);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).convertToDTO(user);

        //assertNotNull(result);
    }

    @Test
    @DisplayName("UnitTest updateUser Success")
    public void testUpdateUser() {
        int userId = 0;
        UserDTO userDTO = new UserDTO();
        User existingUser = new User();
        User updatedUser = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.convertToDTO(updatedUser)).thenReturn(userDTO);

        UserDTO result = userService.updateUser(userId, userDTO);

        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).updateUserFromUserDTO(userDTO, existingUser);
        verify(userRepository, times(1)).save(existingUser);
        verify(userMapper, times(1)).convertToDTO(updatedUser);

        assertNotNull(result);
        assertEquals(userDTO, result);
    }

    @Test
    @DisplayName("UnitTest deleteUser Success")
    public void testDeleteUser() {
        int userId = 1;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(user);
    }

    /********** FAILURE TESTS **********/

    @Test
    @DisplayName("UnitTest updateUser Failure: User Not Found")
    public void testUpdateUserNotFound() {
        // Setup
        int userId = 1;
        UserDTO userDTO = new UserDTO();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Test and Assertions
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userId, userDTO));

        // Verify
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, never()).updateUserFromUserDTO(any(), any());
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).convertToDTO(any());
    }

    @Test
    @DisplayName("UnitTest deleteUser Failure: User Not Found")
    public void testDeleteUserNotFound() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(userId));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).delete(any(User.class));
    }
}
