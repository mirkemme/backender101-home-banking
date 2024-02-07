package org.example.backender101homebanking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class UserRequestDTO {
    private int id;

    @NotBlank(message = "firstName is mandatory")
    private String firstName;

    @NotBlank(message = "lastName is mandatory")
    private String lastName;

    @NotBlank(message = "password is mandatory")
    private String password;

    @NotBlank(message = "email is mandatory")
    @Email(message = "Invalid email format")
    private String email;
}
