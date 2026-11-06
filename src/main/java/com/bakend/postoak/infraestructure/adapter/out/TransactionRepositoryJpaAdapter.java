package com.bakend.postoak.infraestructure.adapter.out;

import com.bakend.postoak.domain.model.Transaction;
import com.bakend.postoak.domain.port.out.TransactionRepositoryPort;
import com.bakend.postoak.infraestructure.persistence.SpringDataTransactionRepository;
import com.bakend.postoak.infraestructure.persistence.TransactionEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TransactionRepositoryJpaAdapter implements TransactionRepositoryPort {

    private final SpringDataTransactionRepository repository;

    public TransactionRepositoryJpaAdapter(SpringDataTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        // Forzamos id a null para evitar conflictos de merge en JPA
        TransactionEntity entity = new TransactionEntity(
                null,
                transaction.getAccountId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCategory(),
                transaction.getMerchantName(),
                transaction.getStatus(),
                transaction.getCreatedAt() != null ? transaction.getCreatedAt() : LocalDateTime.now()
        );
        TransactionEntity savedEntity = repository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public List<Transaction> findByAccountId(UUID accountId) {
        return repository.findByAccountId(accountId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Transaction toDomain(TransactionEntity entity) {
        return new Transaction(
                entity.getId(),
                entity.getAccountId(),
                entity.getAmount(),
                entity.getType(),
                entity.getCategory(),
                entity.getMerchantName(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}