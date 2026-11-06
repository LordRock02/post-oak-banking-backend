package com.bakend.postoak.domain.port.out;

import com.bakend.postoak.domain.model.Account;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepositoryPort {
    Account save(Account account);
    Optional<Account> findById(UUID id);
    List<Account> findByUserId(UUID userId);
}