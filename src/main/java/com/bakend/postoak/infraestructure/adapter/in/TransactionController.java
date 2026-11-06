package com.bakend.postoak.infraestructure.adapter.in;

import com.bakend.postoak.domain.dto.CreateTransactionRequestDTO;
import com.bakend.postoak.domain.model.Transaction;
import com.bakend.postoak.domain.port.in.CreateTransactionUseCase;
import com.bakend.postoak.domain.port.in.GetTransactionsByAccountUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final CreateTransactionUseCase createTransactionUseCase;
    private final GetTransactionsByAccountUseCase getTransactionsByAccountUseCase;

    public TransactionController(CreateTransactionUseCase createTransactionUseCase, GetTransactionsByAccountUseCase getTransactionsByAccountUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.getTransactionsByAccountUseCase = getTransactionsByAccountUseCase;
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody CreateTransactionRequestDTO request) {
        Transaction transaction = new Transaction(
                null,
                request.getAccountId(),
                request.getAmount(),
                request.getType(),
                request.getCategory(),
                request.getMerchantName(),
                request.getStatus(),
                null
        );
        Transaction createdTransaction = createTransactionUseCase.createTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccount(@PathVariable UUID accountId) {
        List<Transaction> transactions = getTransactionsByAccountUseCase.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }
}