package com.bakend.postoak.application.service;

import com.bakend.postoak.domain.model.Transaction;
import com.bakend.postoak.domain.port.in.GetTransactionsByAccountUseCase;
import com.bakend.postoak.domain.port.out.TransactionRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetTransactionsByAccountService implements GetTransactionsByAccountUseCase {

    private final TransactionRepositoryPort transactionRepositoryPort;

    public GetTransactionsByAccountService(TransactionRepositoryPort transactionRepositoryPort) {
        this.transactionRepositoryPort = transactionRepositoryPort;
    }

    @Override
    public List<Transaction> getTransactionsByAccountId(UUID accountId) {
        return transactionRepositoryPort.findByAccountId(accountId);
    }
}