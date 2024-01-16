package org.example.backender101homebanking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
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
}
