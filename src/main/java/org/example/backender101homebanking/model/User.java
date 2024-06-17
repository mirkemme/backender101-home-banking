package org.example.backender101homebanking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Data
@RequiredArgsConstructor
@Table (name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

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

    @Column(name = "username", nullable = false, unique = true)
    @NotBlank(message = "username is mandatory")
    private String username;

    private boolean enabled;

    @ManyToMany(mappedBy = "users")
    private List<Account> accounts;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(  name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}