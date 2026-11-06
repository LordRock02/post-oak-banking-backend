package com.bakend.postoak.aplication;

import com.bakend.postoak.infraestructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void generateToken_ShouldReturnNonEmptyString() {
        // 1. Arrange
        String email = "usuario@test.com";
        String nombre = "Juan Perez";

        // 2. Act
        String token = jwtService.generateToken(email, nombre);

        // 3. Assert
        assertNotNull(token);
        assertFalse(token.isEmpty(), "El token no debería estar vacío");

        // Verificación extra: El token JWT debe tener 3 partes separadas por puntos
        assertEquals(3, token.split("\\.").length, "El token debe tener formato Header.Payload.Signature");
    }

    @Test
    void extractUsername_ShouldReturnCorrectEmail_WhenTokenIsValid() {
        // 1. Arrange
        String emailEsperado = "juan@test.com";
        String nombre = "Juan";
        // Generamos un token REAL usando el mismo servicio
        String token = jwtService.generateToken(emailEsperado, nombre);

        // 2. Act
        String emailExtraido = jwtService.extractUsername(token);

        // 3. Assert
        assertEquals(emailEsperado, emailExtraido);
    }

    @Test
    void extractUsername_ShouldThrowException_WhenTokenIsInvalid() {
        // 1. Arrange
        String tokenInvalido = "este.no.es.un.token.valido";

        // 2. Act & Assert
        // JJWT lanza JwtException (o subclases) cuando el formato o la firma fallan
        assertThrows(Exception.class, () -> {
            jwtService.extractUsername(tokenInvalido);
        });
    }
}
