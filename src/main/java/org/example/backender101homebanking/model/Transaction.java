package org.example.backender101homebanking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "account_number", nullable = false)
    @NotBlank(message = "account_number is mandatory")
    private Account account;
    @Column(name = "type", nullable = false)
    @NotBlank(message = "type is mandatory")
    private String type;
    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    @NotBlank(message = "amount is mandatory")
    private BigDecimal amount;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp", nullable = false)
    @NotBlank(message = "timestamp is mandatory")
    private Date timestamp;
    @Column(name = "currency", nullable = false)
    @NotBlank(message = "currency is mandatory")
    private String currency;

    public Transaction(@JsonProperty("id") Long id,
                       @JsonProperty("account") Account account,
                       @JsonProperty("type") String type,
                       @JsonProperty("amount") BigDecimal amount,
                       @JsonProperty("timestamp") Date timestamp,
                       @JsonProperty("currency") String currency) {
        this.id = id;
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
        this.currency = currency;
    }

    public Transaction() {

    }

    public Account getAccount() {
        return account;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
