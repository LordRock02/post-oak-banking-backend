package com.bakend.postoak.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private UUID id;
    private UUID userId;
    private String accountNumber;
    private String name;
    private BigDecimal balance;
    private String currency;
    private LocalDateTime createdAt;
}