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
public class Card {
    private UUID id;
    private UUID accountId;
    private String cardNumberMasked;
    private String cardholderName;
    private String type;           // VIRTUAL o PHYSICAL
    private BigDecimal spendingLimit;
    private String status;         // ACTIVE, FROZEN
    private LocalDateTime createdAt;
}