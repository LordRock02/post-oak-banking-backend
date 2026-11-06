package com.bakend.postoak.application.service;

import com.bakend.postoak.domain.model.Account;
import com.bakend.postoak.domain.port.in.CreateAccountUseCase;
import com.bakend.postoak.domain.port.out.AccountRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CreateAccountService implements CreateAccountUseCase {

    private final AccountRepositoryPort accountRepositoryPort;

    public CreateAccountService(AccountRepositoryPort accountRepositoryPort) {
        this.accountRepositoryPort = accountRepositoryPort;
    }

    @Override
    public Account createAccount(Account account) {
        if (account.getAccountNumber() == null || account.getAccountNumber().isBlank()) {
            account.setAccountNumber("ACC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        account.setCreatedAt(LocalDateTime.now());
        return accountRepositoryPort.save(account);
    }
}