package org.example.backender101homebanking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@Table (name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column (name = "firstName", nullable = false)
    @NotBlank(message = "firstName is mandatory")
    private String firstName;

    @Column (name = "lastName", nullable = false)
    @NotBlank(message = "lastName is mandatory")
    private String lastName;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "password is mandatory")
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "email is mandatory")
    private String email;

    @ManyToMany(mappedBy = "users")
    private List<Account> accounts;
}