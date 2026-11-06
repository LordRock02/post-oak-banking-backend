package com.bakend.postoak.aplication;

import com.bakend.postoak.application.service.AddUserService;
import com.bakend.postoak.domain.model.User;
import com.bakend.postoak.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddUserUseCaseTest {

    private UserRepositoryPort userRepositoryPort;
    private PasswordEncoder passwordEncoder;
    private AddUserService registrarUsuarioService;

    @BeforeEach
    void setUp() {
        userRepositoryPort = mock(UserRepositoryPort.class);
        passwordEncoder = mock(PasswordEncoder.class);
        registrarUsuarioService = new AddUserService(
                userRepositoryPort,
                passwordEncoder
        );
    }

    @Test
    void debeRegistrarUsuarioConPasswordCodificada() {
        // Arrange
        User user = new User(
                null,
                "pablo@email.com",
                "passwordPlano",
                "Pablo",
                "Post Oak",
                null
        );

        when(passwordEncoder.encode("passwordPlano"))
                .thenReturn("passwordCodificada");

        ArgumentCaptor<User> usuarioCaptor =
                ArgumentCaptor.forClass(User.class);

        // Act - Actualizado a registrar()
        registrarUsuarioService.register(user);

        // Assert
        verify(passwordEncoder, times(1))
                .encode("passwordPlano");

        // Actualizado a guardar()
        verify(userRepositoryPort, times(1))
                .save(usuarioCaptor.capture());

        User userGuardado = usuarioCaptor.getValue();

        assertEquals("Pablo", userGuardado.getFullName());
        assertEquals("pablo@email.com", userGuardado.getEmail());
        assertEquals("passwordCodificada", userGuardado.getPasswordHash());
    }
}