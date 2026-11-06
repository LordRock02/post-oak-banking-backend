package com.bakend.postoak.application.service;

import com.bakend.postoak.domain.model.Transaction;
import com.bakend.postoak.domain.port.in.CreateTransactionUseCase;
import com.bakend.postoak.domain.port.out.TransactionRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CreateTransactionService implements CreateTransactionUseCase {

    private final TransactionRepositoryPort transactionRepositoryPort;

    public CreateTransactionService(TransactionRepositoryPort transactionRepositoryPort) {
        this.transactionRepositoryPort = transactionRepositoryPort;
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        if (transaction.getStatus() == null || transaction.getStatus().isBlank()) {
            transaction.setStatus("COMPLETED");
        }
        transaction.setCreatedAt(LocalDateTime.now());
        return transactionRepositoryPort.save(transaction);
    }
}