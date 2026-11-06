package com.bakend.postoak.domain.port.in;

import com.bakend.postoak.domain.model.Account;
import java.util.List;
import java.util.UUID;

public interface GetAccountsByUserUseCase {
    List<Account> getAccountsByUserId(UUID userId);
}