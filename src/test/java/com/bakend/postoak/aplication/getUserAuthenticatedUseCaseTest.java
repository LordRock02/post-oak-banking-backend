package com.bakend.postoak.aplication;

import com.bakend.postoak.application.service.GetUserAuthenticatedService;
import com.bakend.postoak.domain.model.User;
import com.bakend.postoak.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserAuthenticatedUseCaseTest {

    private UserRepositoryPort userRepositoryPort;
    private GetUserAuthenticatedService getUserAuthenticatedService;

    @BeforeEach
    void setUp() {
        userRepositoryPort = mock(UserRepositoryPort.class);
        getUserAuthenticatedService =
                new GetUserAuthenticatedService(userRepositoryPort);
    }

    @Test
    void shouldReturnUserWhenExists() {
        // Arrange
        String email = "pablo@email.com";
        User user = new User(
                UUID.randomUUID(),
                email,
                "passwordHash",
                "Pablo",
                "Post Oak",
                LocalDateTime.now()
        );

        when(userRepositoryPort.findByEmail(email))
                .thenReturn(Optional.of(user));

        // Act
        User result =
                getUserAuthenticatedService.getCurrentUser(email);

        // Assert
        assertNotNull(result);
        assertEquals("Pablo", result.getFullName());
        assertEquals(email, result.getEmail());

        verify(userRepositoryPort, times(1))
                .findByEmail(email);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        // Arrange
        String email = "noexiste@email.com";

        when(userRepositoryPort.findByEmail(email))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> getUserAuthenticatedService.getCurrentUser(email)
        );

        assertEquals("Usuario no encontrado", exception.getMessage());

        verify(userRepositoryPort, times(1))
                .findByEmail(email);
    }
}