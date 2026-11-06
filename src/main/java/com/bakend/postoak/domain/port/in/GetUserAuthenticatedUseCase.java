package com.bakend.postoak.domain.port.in;

import com.bakend.postoak.domain.model.User;

public interface GetUserAuthenticatedUseCase {
    User getCurrentUser(String email);
}

