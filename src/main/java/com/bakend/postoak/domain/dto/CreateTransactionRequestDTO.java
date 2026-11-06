package com.bakend.postoak.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionRequestDTO {
    private UUID accountId;
    private BigDecimal amount;
    private String type;         // DEBIT o CREDIT
    private String category;     // Software, Payroll, etc.
    private String merchantName;
    private String status;       // COMPLETED, PENDING, FAILED
}