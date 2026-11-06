package com.bakend.postoak.aplication;

import com.bakend.postoak.application.config.BeanConfig;
import com.bakend.postoak.application.service.AddUserService;
import com.bakend.postoak.domain.port.in.AddUserUseCase;
import com.bakend.postoak.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class BeanConfigTest {

    @Test
    void deberiaCrearBeanRegistrarUsuarioUseCase() {
        // Arrange
        BeanConfig beanConfig = new BeanConfig();

        UserRepositoryPort userRepositoryPort =
                mock(UserRepositoryPort.class);

        PasswordEncoder passwordEncoder =
                mock(PasswordEncoder.class);

        // Act
        AddUserUseCase useCase =
                beanConfig.registrarUsuarioUseCase(
                        userRepositoryPort,
                        passwordEncoder
                );

        // Assert
        assertNotNull(useCase);
        assertTrue(useCase instanceof AddUserService);
    }
}
