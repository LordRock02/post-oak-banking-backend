package com.bakend.postoak.domain.port.in;

import com.bakend.postoak.domain.model.User;

public interface LoginUserUseCase {
    User login(String email, String password);
}

