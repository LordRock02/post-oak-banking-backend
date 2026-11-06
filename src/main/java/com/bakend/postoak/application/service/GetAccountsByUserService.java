package com.bakend.postoak.application.service;

import com.bakend.postoak.domain.model.Account;
import com.bakend.postoak.domain.port.in.GetAccountsByUserUseCase;
import com.bakend.postoak.domain.port.out.AccountRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetAccountsByUserService implements GetAccountsByUserUseCase {

    private final AccountRepositoryPort accountRepositoryPort;

    public GetAccountsByUserService(AccountRepositoryPort accountRepositoryPort) {
        this.accountRepositoryPort = accountRepositoryPort;
    }

    @Override
    public List<Account> getAccountsByUserId(UUID userId) {
        return accountRepositoryPort.findByUserId(userId);
    }
}