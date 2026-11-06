package com.bakend.postoak.infraestructure.adapter.out;

import com.bakend.postoak.domain.model.Account;
import com.bakend.postoak.domain.port.out.AccountRepositoryPort;
import com.bakend.postoak.infraestructure.persistence.AccountEntity;
import com.bakend.postoak.infraestructure.persistence.SpringDataAccountRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AccountRepositoryJpaAdapter implements AccountRepositoryPort {

    private final SpringDataAccountRepository repository;

    public AccountRepositoryJpaAdapter(SpringDataAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Account save(Account account) {
        // Forzamos ID a null si es nuevo para evitar conflictos con JPA merge
        AccountEntity entity = new AccountEntity(
                null,
                account.getUserId(),
                account.getAccountNumber(),
                account.getName(),
                account.getBalance(),
                account.getCurrency(),
                account.getCreatedAt() != null ? account.getCreatedAt() : LocalDateTime.now()
        );
        AccountEntity savedEntity = repository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Account> findByUserId(UUID userId) {
        return repository.findByUserId(userId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Account toDomain(AccountEntity entity) {
        return new Account(
                entity.getId(),
                entity.getUserId(),
                entity.getAccountNumber(),
                entity.getName(),
                entity.getBalance(),
                entity.getCurrency(),
                entity.getCreatedAt()
        );
    }
}