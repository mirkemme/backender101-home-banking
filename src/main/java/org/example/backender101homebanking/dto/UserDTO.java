package org.example.backender101homebanking.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class UserDTO {
    private long id;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    @Email(message = "Invalid email format")
    private String email;

    private List<AccountRequestDTO> accounts;
}
