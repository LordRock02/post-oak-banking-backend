package com.bakend.postoak.domain.port.in;

import com.bakend.postoak.domain.model.Transaction;

public interface CreateTransactionUseCase {
    Transaction createTransaction(Transaction transaction);
}