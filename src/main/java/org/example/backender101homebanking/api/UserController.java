package org.example.backender101homebanking.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backender101homebanking.dto.ApiResponseDto;
import org.example.backender101homebanking.dto.SignInRequestDto;
import org.example.backender101homebanking.dto.SignUpRequestDto;
import org.example.backender101homebanking.dto.UserDTO;
import org.example.backender101homebanking.service.AuthService;
import org.example.backender101homebanking.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public @ResponseBody ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/me")
    public @ResponseBody ResponseEntity<UserDTO> getUserById(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        UserDTO user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<ApiResponseDto<?>> addUser(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        return authService.signUpUser(signUpRequestDto);
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<ApiResponseDto<?>> signInUser(@RequestBody @Valid SignInRequestDto signInRequestDto){
        return authService.signInUser(signInRequestDto);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser(HttpServletRequest request, @Valid @RequestBody UserDTO userDTO) {
        Long userId = (Long) request.getAttribute("userId");
        UserDTO updatedUser = userService.updateUser(userId, userDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

