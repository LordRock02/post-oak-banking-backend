package com.bakend.postoak.aplication;

import com.bakend.postoak.application.service.RefreshTokenService;
import com.bakend.postoak.infraestructure.persistence.RefreshTokenEntity;
import com.bakend.postoak.infraestructure.persistence.RefreshTokenJpaRepository;
import com.bakend.postoak.infraestructure.persistence.UserEntity;
import com.bakend.postoak.infraestructure.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {
    @Mock
    private RefreshTokenJpaRepository refreshTokenRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    void refresh_ShouldReturnNewToken_WhenTokenIsValidAndNotExpired() {
        // 1. Arrange
        String uuidToken = "uuid-valido-123";
        String expectedNewJwt = "nuevo.token.jwt";

        // Preparamos un Usuario Dummy
        UserEntity usuarioMock = new UserEntity();
        usuarioMock.setEmail("test@email.com");
        usuarioMock.setFullName("Juan");

        // Preparamos el RefreshTokenEntity que devolverá la base de datos
        // Importante: La fecha de expiración debe ser en el FUTURO (plus)
        RefreshTokenEntity tokenEntity = new RefreshTokenEntity();
        tokenEntity.setToken(uuidToken);
        tokenEntity.setUsuario(usuarioMock);
        tokenEntity.setExpiryDate(Instant.now().plus(1, ChronoUnit.HOURS));

        // Simulamos el comportamiento de las dependencias
        when(refreshTokenRepository.findByToken(uuidToken)).thenReturn(Optional.of(tokenEntity));
        when(jwtService.generateToken(usuarioMock.getEmail(), usuarioMock.getFullName())).thenReturn(expectedNewJwt);

        // 2. Act
        String resultado = refreshTokenService.refresh(uuidToken);

        // 3. Assert
        assertEquals(expectedNewJwt, resultado);
        verify(refreshTokenRepository).findByToken(uuidToken);
        verify(jwtService).generateToken(anyString(), anyString());
    }

    @Test
    void refresh_ShouldThrowException_WhenTokenDoesNotExist() {
        // 1. Arrange
        String uuidToken = "token-inexistente";

        // Simulamos que la base de datos no encuentra nada (Empty)
        when(refreshTokenRepository.findByToken(uuidToken)).thenReturn(Optional.empty());

        // 2. Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            refreshTokenService.refresh(uuidToken);
        });

        assertEquals("Refresh token inválido", exception.getMessage());

        // Nos aseguramos de que NUNCA se intente generar un token si falló la búsqueda
        verify(jwtService, never()).generateToken(anyString(), anyString());
    }

    @Test
    void refresh_ShouldThrowException_WhenTokenIsExpired() {
        // 1. Arrange
        String uuidToken = "token-vencido";

        RefreshTokenEntity tokenEntity = new RefreshTokenEntity();
        tokenEntity.setToken(uuidToken);
        // Importante: La fecha de expiración es en el PASADO (minus)
        tokenEntity.setExpiryDate(Instant.now().minus(1, ChronoUnit.HOURS));

        when(refreshTokenRepository.findByToken(uuidToken)).thenReturn(Optional.of(tokenEntity));

        // 2. Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            refreshTokenService.refresh(uuidToken);
        });

        assertEquals("Refresh token expirado", exception.getMessage());
        verify(jwtService, never()).generateToken(anyString(), anyString());
    }
}
