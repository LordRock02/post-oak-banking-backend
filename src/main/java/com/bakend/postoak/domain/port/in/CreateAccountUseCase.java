package com.bakend.postoak.domain.port.in;

import com.bakend.postoak.domain.model.Account;

public interface CreateAccountUseCase {
    Account createAccount(Account account);
}