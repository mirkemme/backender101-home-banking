package org.example.backender101homebanking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @Column(name = "number")
    private String number;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    public Account(@JsonProperty("number") String number,
                   @JsonProperty("user") User user,
                   @JsonProperty("balance") BigDecimal balance,
                   @JsonProperty("transactions") List<Transaction> transactions) {
        this.number = number;
        this.user = user;
        this.balance = balance;
        this.transactions = transactions;
    }

    public Account() {

    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getNumber() {
        return number;
    }

    public User getUser() {
        return user;
    }
}
