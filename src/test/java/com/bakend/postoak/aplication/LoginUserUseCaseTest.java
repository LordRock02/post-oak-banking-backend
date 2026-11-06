package com.bakend.postoak.aplication;


import com.bakend.postoak.application.service.LoginUserService;
import com.bakend.postoak.domain.model.User;
import com.bakend.postoak.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginUserUseCaseTest {
    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginUserService loginUsuarioService;

    @Test
    void login_ShouldReturnUser_WhenCredentialsAreValid() {
        // 1. Arrange
        String email = "test@ejemplo.com";
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";

        // MOCKEAMOS la entidad Usuario para no inventar su constructor
        User userMock = mock(User.class);
        when(userMock.getPasswordHash()).thenReturn(encodedPassword);

        // Simulamos el comportamiento de tus dependencias exactas
        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.of(userMock));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        // 2. Act
        User result = loginUsuarioService.login(email, rawPassword);

        // 3. Assert
        assertNotNull(result);
        assertEquals(userMock, result);
        verify(userRepositoryPort).findByEmail(email);
    }

    @Test
    void login_ShouldThrowException_WhenUserNotFound() {
        // 1. Arrange
        String email = "noexiste@ejemplo.com";
        String rawPassword = "123";

        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.empty());

        // 2. Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loginUsuarioService.login(email, rawPassword);
        });

        // Verificamos que el mensaje sea EXACTAMENTE el de tu código
        assertEquals("Credenciales inválidas", exception.getMessage());
    }

    @Test
    void login_ShouldThrowException_WhenPasswordDoesNotMatch() {
        // 1. Arrange
        String email = "test@ejemplo.com";
        String rawPassword = "passwordErronea";
        String encodedPassword = "realPassword";

        User userMock = mock(User.class);
        when(userMock.getPasswordHash()).thenReturn(encodedPassword);

        when(userRepositoryPort.findByEmail(email)).thenReturn(Optional.of(userMock));
        // Simulamos que el encoder dice que NO coinciden
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        // 2. Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loginUsuarioService.login(email, rawPassword);
        });

        assertEquals("Credenciales inválidas", exception.getMessage());
    }


}
