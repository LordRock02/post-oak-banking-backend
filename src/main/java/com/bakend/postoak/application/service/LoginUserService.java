package com.bakend.postoak.application.service;

import com.bakend.postoak.domain.model.User;
import com.bakend.postoak.domain.port.in.LoginUserUseCase;
import com.bakend.postoak.domain.port.out.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;



public class LoginUserService implements LoginUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public LoginUserService(
            UserRepositoryPort userRepositoryPort,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User login(String email, String password) {

        User user = userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        return user;
    }
}


