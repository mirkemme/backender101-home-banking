package org.example.backender101homebanking.controller;

import org.example.backender101homebanking.api.UserController;
import org.example.backender101homebanking.dto.UserDTO;
import org.example.backender101homebanking.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("GET /api/v1/users/all")
    public void testGetAllUsers() throws Exception {
        List<UserDTO> users = Arrays.asList(new UserDTO(), new UserDTO());

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(users.size()));
    }

    @Test
    @DisplayName("GET /api/v1/users/1")
    public void testGetUserById() throws Exception {
        int userId = 1;
        UserDTO userDTO = new UserDTO();

        when(userService.getUserById(userId)).thenReturn(userDTO);

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("POST /api/v1/users")
    public void testAddUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        UserDTO savedUserDTO = new UserDTO();

        when(userService.addUser(any(UserDTO.class))).thenReturn(savedUserDTO);

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"firstName\": \"Pippo\","
                                + "\"lastName\": \"Di Pippo\","
                                + "\"password\": \"123456\","
                                + "\"email\": \"pippo@email.com\""
                                + "}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("PUT /api/v1/users/1")
    public void testUpdateUser() throws Exception {
        int userId = 1;
        UserDTO userDTO = new UserDTO();
        UserDTO updatedUserDTO = new UserDTO();

        doReturn(updatedUserDTO).when(userService).updateUser(eq(userId), any(UserDTO.class));

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"firstName\": \"Test\","
                                + "\"lastName\": \"Test\","
                                + "\"password\": \"123456\","
                                + "\"email\": \"test@email.com\""
                                + "}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("DELETE /api/v1/users/1")
    public void testDeleteUser() throws Exception {
        int userId = 1;

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/{userId}", userId))
                .andExpect(status().isOk());
    }
}
