package org.example.backender101homebanking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table (name = "user")
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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Account> accounts;

    public User(@JsonProperty("id") int id,
                @JsonProperty("firstNAme") String firstName,
                @JsonProperty("lastName") String lastName,
                @JsonProperty("password") String password,
                @JsonProperty("email")String email,
                @JsonProperty("account") List<Account> accounts) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.accounts = accounts;
    }

    public User() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}