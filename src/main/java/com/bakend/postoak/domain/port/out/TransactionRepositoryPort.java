package com.bakend.postoak.domain.port.out;

import com.bakend.postoak.domain.model.Transaction;
import java.util.List;
import java.util.UUID;

public interface TransactionRepositoryPort {
    Transaction save(Transaction transaction);
    List<Transaction> findByAccountId(UUID accountId);
}