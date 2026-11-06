package com.bakend.postoak.application.service;

import com.bakend.postoak.domain.model.User;
import com.bakend.postoak.domain.port.in.GetUserAuthenticatedUseCase;
import com.bakend.postoak.domain.port.out.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class GetUserAuthenticatedService implements GetUserAuthenticatedUseCase {

    private final UserRepositoryPort userRepositoryPort;

    public GetUserAuthenticatedService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public User getCurrentUser(String email) {
        return userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}