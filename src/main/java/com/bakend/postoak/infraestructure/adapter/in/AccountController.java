package com.bakend.postoak.infraestructure.adapter.in;

import com.bakend.postoak.domain.dto.CreateAccountRequestDTO;
import com.bakend.postoak.domain.model.Account;
import com.bakend.postoak.domain.port.in.CreateAccountUseCase;
import com.bakend.postoak.domain.port.in.GetAccountsByUserUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountsByUserUseCase getAccountsByUserUseCase;

    public AccountController(CreateAccountUseCase createAccountUseCase, GetAccountsByUserUseCase getAccountsByUserUseCase) {
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountsByUserUseCase = getAccountsByUserUseCase;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountRequestDTO request) {
        Account account = new Account(
                null,
                request.getUserId(),
                null, // Se autogenera en el servicio
                request.getName(),
                request.getBalance(),
                request.getCurrency(),
                null
        );
        Account createdAccount = createAccountUseCase.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUser(@PathVariable UUID userId) {
        List<Account> accounts = getAccountsByUserUseCase.getAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }
}