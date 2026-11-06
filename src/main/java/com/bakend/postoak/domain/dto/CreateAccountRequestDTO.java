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
public class CreateAccountRequestDTO {
    private UUID userId;
    private String name;
    private BigDecimal balance;
    private String currency;
}