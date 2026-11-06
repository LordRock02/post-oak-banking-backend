package com.bakend.postoak.infraestructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String type; // DEBIT o CREDIT

    @Column(nullable = false)
    private String category; // Software, Payroll, etc.

    @Column(name = "merchant_name")
    private String merchantName;

    @Column(nullable = false)
    private String status; // COMPLETED, PENDING, FAILED

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public TransactionEntity(UUID id, UUID accountId, BigDecimal amount, String type, String category, String merchantName, String status, LocalDateTime createdAt) {
        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.merchantName = merchantName;
        this.status = status;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }
}