package com.bakend.postoak.aplication;

import com.bakend.postoak.infraestructure.persistence.SpringDataUserRepository;
import com.bakend.postoak.infraestructure.persistence.UserEntity;
import com.bakend.postoak.infraestructure.security.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    private SpringDataUserRepository repository;
    private CustomUserDetailsService service;

    @BeforeEach
    void setUp() {
        repository = mock(SpringDataUserRepository.class);
        service = new CustomUserDetailsService(repository);
    }

    @Test
    void deberiaRetornarUserDetailsCuandoUsuarioExiste() {
        // Arrange
        UserEntity usuario = new UserEntity();
        usuario.setEmail("pablo@email.com");
        // Actualizado a setPasswordHash para coincidir con la entidad JPA del ERD
        usuario.setPasswordHash("password-encriptado");

        when(repository.findByEmail("pablo@email.com"))
                .thenReturn(Optional.of(usuario));

        // Act
        UserDetails userDetails =
                service.loadUserByUsername("pablo@email.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals("pablo@email.com", userDetails.getUsername());
        verify(repository, times(1))
                .findByEmail("pablo@email.com");
    }

    @Test
    void deberiaLanzarExcepcionCuandoUsuarioNoExiste() {
        // Arrange
        when(repository.findByEmail("noexiste@email.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserByUsername("noexiste@email.com")
        );

        verify(repository, times(1))
                .findByEmail("noexiste@email.com");
    }
}