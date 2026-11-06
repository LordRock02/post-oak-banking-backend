package com.bakend.postoak.domain.port.in;

import com.bakend.postoak.domain.model.User;

public interface AddUserUseCase {
    void register(User user);
}
