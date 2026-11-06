package com.bakend.postoak.application.service;

import com.bakend.postoak.domain.model.User;
import com.bakend.postoak.domain.port.in.AddUserUseCase;
import com.bakend.postoak.domain.port.out.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

public class AddUserService implements AddUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public AddUserService(UserRepositoryPort userRepositoryPort,
                          PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(User user) {

        // Creamos el usuario seguro encriptando la contraseña y rellenando los campos del ERD
        User userSeguro = new User(
                user.getId() != null ? user.getId() : UUID.randomUUID(),
                user.getEmail(),
                passwordEncoder.encode(user.getPasswordHash() != null ? user.getPasswordHash() : user.getPasswordHash()),
                user.getFullName(),
                user.getCompanyName(),
                user.getCreatedAt() != null ? user.getCreatedAt() : LocalDateTime.now()
        );

        userRepositoryPort.save(userSeguro);
    }
}