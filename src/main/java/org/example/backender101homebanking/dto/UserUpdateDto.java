package org.example.backender101homebanking.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class UserUpdateDto {

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    @Email(message = "Invalid email format")
    private String email;
}