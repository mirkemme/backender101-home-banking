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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("GET /rest/widget/1")
    public void testGetUserById() throws Exception {
        int userId = 1;
        UserDTO userDTO = new UserDTO();

        when(userService.getUserById(userId)).thenReturn(userDTO);

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(get("/api/v1/users/{userId}", userId))
                .andExpect(status().isOk());
    }
}
