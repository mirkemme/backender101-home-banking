package org.example.backender101homebanking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@Data
@RequiredArgsConstructor
@Table(name = "transaction")
public class Transaction {
    public enum TransactionType {
        DEPOSIT,
        WITHDRAW;
        public String toString() {
            return name().toLowerCase();
        }
        public static TransactionType fromString(String value) {
            return valueOf(value.toUpperCase());
        }
    }

    public enum CurrencyType {
        EURO,
        DOLLAR,
        YEN;
        public String toString() {
            return name().toLowerCase();
        }
        public static CurrencyType fromString(String value) {
            return valueOf(value.toUpperCase());
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_number", nullable = false)
    private Account account;

    @Column(name = "type", nullable = false)
    @NotNull(message = "type is mandatory")
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    @NotNull(message = "amount is mandatory")
    @Positive(message = "amount must be positive")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp", nullable = false)
    @NotNull(message = "timestamp is mandatory")
    private Date timestamp;

    @Column(name = "currency", nullable = false)
    @NotNull(message = "currency is mandatory")
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;
}
