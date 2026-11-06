package com.bakend.postoak.domain.port.in;

import com.bakend.postoak.domain.model.Transaction;
import java.util.List;
import java.util.UUID;

public interface GetTransactionsByAccountUseCase {
    List<Transaction> getTransactionsByAccountId(UUID accountId);
}