package org.example.backender101homebanking.service;

import org.example.backender101homebanking.dto.AccountRequestDTO;
import org.example.backender101homebanking.dto.UserDTO;
import org.example.backender101homebanking.dto.UserUpdateDto;
import org.example.backender101homebanking.exception.ResourceNotFoundException;
import org.example.backender101homebanking.mapper.AccountMapper;
import org.example.backender101homebanking.mapper.UserMapper;
import org.example.backender101homebanking.model.Account;
import org.example.backender101homebanking.model.User;
import org.example.backender101homebanking.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.example.backender101homebanking.utils.TestObjectFactory.buildAccount;
import static org.example.backender101homebanking.utils.TestObjectFactory.buildUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private AccountMapper accountMapper;
    @InjectMocks
    private UserServiceImpl userService;
    @Test
    @DisplayName("UnitTest getAllUsers Success")
    public void testGetAllUsers() {
        User user = buildUser("name-user1", "surname-user1", "username1", "user1@email.com", "123456789");
        Account account1 = buildAccount("IT60X0542811101000000654321", new BigDecimal("1000.00"), Collections.singletonList(user));
        Account account2 = buildAccount("IT60X0542811101000000654322", new BigDecimal("2000.00"), Collections.singletonList(user));
        List<Account> accounts = Arrays.asList(account1, account2);
        user.setAccounts(accounts);

        when(accountMapper.convertToAccountRequestDTO(account1)).thenReturn(new AccountRequestDTO());
        when(accountMapper.convertToAccountRequestDTO(account2)).thenReturn(new AccountRequestDTO());
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        when(userMapper.convertToDTO(any(User.class))).thenReturn(new UserDTO());

        List<UserDTO> result = userService.getAllUsers();

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).convertToDTO(any(User.class));

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("UnitTest findById Success")
    public void testGetUserById() {
        long userId = 1;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDTO userDTO = new UserDTO();
        when(userMapper.convertToDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(userId);

        assertEquals(userDTO, result);
    }

    @Test
    @DisplayName("UnitTest addUser Success")
    public void testAddUser() {
        UserDTO userDTO = new UserDTO();
        User user = new User();

        when(userRepository.save(user)).thenReturn(user);

        userService.addUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("UnitTest updateUser Success")
    public void testUpdateUser() {
        long userId = 0;
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        UserDTO userDTO = new UserDTO();
        User existingUser = new User();
        User updatedUser = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.convertToDTO(updatedUser)).thenReturn(userDTO);

        UserDTO result = userService.updateUser(userId, userUpdateDto);

        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).updateUserFromUserUpdateDto(userUpdateDto, existingUser);
        verify(userRepository, times(1)).save(existingUser);
        verify(userMapper, times(1)).convertToDTO(updatedUser);

        assertNotNull(result);
        assertEquals(userDTO, result);
    }

    @Test
    @DisplayName("UnitTest deleteUser Success")
    public void testDeleteUser() {
        long userId = 1;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    @DisplayName("UnitTest existsByUsername Success")
    public void testExistsByUsername() {
        String existingUsername = "username-user1";
        User user = new User();

        when(userRepository.existsByUsername(existingUsername)).thenReturn(true);

        boolean exists = userService.existsByUsername(existingUsername);

        assertTrue(exists, "User should exist by username");
        verify(userRepository, times(1)).existsByUsername(existingUsername);
    }

    @Test
    @DisplayName("UnitTest existsByEmail Success")
    public void testExistsByEmail() {
        String existingEmail = "user1@email.com";
        User user = new User();

        when(userRepository.existsByEmail(existingEmail)).thenReturn(true);

        boolean exists = userService.existsByEmail(existingEmail);

        assertTrue(exists, "User should exist by email");
        verify(userRepository, times(1)).existsByEmail(existingEmail);
    }

    /********** FAILURE TESTS **********/

    @Test
    @DisplayName("UnitTest updateUser Failure: User Not Found")
    public void testUpdateUserNotFound() {
        long userId = 1;
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userId, userUpdateDto));

        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, never()).updateUserFromUserUpdateDto(any(), any());
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).convertToDTO(any());
    }

    @Test
    @DisplayName("UnitTest findById Failure: User not found")
    void testFindByIdNotFound(){
        long userId = 2;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    @DisplayName("UnitTest deleteUser Failure: User Not Found")
    public void testDeleteUserNotFound() {
        long userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(userId));

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    @DisplayName("UnitTest existsByUsername Failure: User Not Found")
    public void testExistsByUsernameNotFound() {
        String notExistingUsername = "username-user1";
        User user = new User();

        when(userRepository.existsByUsername(notExistingUsername)).thenReturn(false);

        boolean exists = userService.existsByUsername(notExistingUsername);

        assertFalse(exists, "User should not exist by username");
        verify(userRepository, times(1)).existsByUsername(notExistingUsername);
    }

    @Test
    @DisplayName("UnitTest existsByEmail Failure: User Not Found")
    public void testExistsByEmailNotFound() {
        String notExistingEmail = "user1@email.com";
        User user = new User();

        when(userRepository.existsByEmail(notExistingEmail)).thenReturn(false);

        boolean exists = userService.existsByEmail(notExistingEmail);

        assertFalse(exists, "User should exist by email");
        verify(userRepository, times(1)).existsByEmail(notExistingEmail);
    }
}
