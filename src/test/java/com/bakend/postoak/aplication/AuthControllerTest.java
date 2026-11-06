package com.bakend.postoak.aplication;

import com.bakend.postoak.application.service.RefreshTokenService;
import com.bakend.postoak.domain.dto.LoginRequestDTO;
import com.bakend.postoak.domain.dto.RefreshRequestDTO;
import com.bakend.postoak.domain.model.User;
import com.bakend.postoak.domain.port.in.LoginUserUseCase;
import com.bakend.postoak.infraestructure.adapter.in.AuthController;
import com.bakend.postoak.infraestructure.persistence.RefreshTokenEntity;
import com.bakend.postoak.infraestructure.persistence.RefreshTokenJpaRepository;
import com.bakend.postoak.infraestructure.persistence.SpringDataUserRepository;
import com.bakend.postoak.infraestructure.persistence.UserEntity;
import com.bakend.postoak.infraestructure.security.JwtService;
import com.bakend.postoak.infraestructure.security.CustomUserDetailsService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean private LoginUserUseCase loginUserUseCase;
    @MockitoBean private JwtService jwtService;
    @MockitoBean private RefreshTokenJpaRepository refreshTokenRepository;
    @MockitoBean private SpringDataUserRepository usuarioJpaRepository;
    @MockitoBean private RefreshTokenService refreshTokenService;
    @MockitoBean private CustomUserDetailsService customUserDetailsService;

    @Test
    void login_ShouldReturn200_AndAuthResponse_WhenCredentialsAreValid() throws Exception {
        // Arrange
        String email = "juan@test.com";
        String password = "123";
        String nombre = "Juan Perez";
        String fakeToken = "fake-jwt-token";

        LoginRequestDTO request = new LoginRequestDTO(email, password);

        User userDomain = mock(User.class);
        when(userDomain.getEmail()).thenReturn(email);
        when(userDomain.getFullName()).thenReturn(nombre);

        UserEntity entityMock = new UserEntity();
        entityMock.setId(UUID.randomUUID()); // Actualizado a UUID según el ERD
        entityMock.setEmail(email);
        entityMock.setFullName(nombre);
        entityMock.setPasswordHash("encodedPass"); // Corregido a setPasswordHash

        when(loginUserUseCase.login(email, password)).thenReturn(userDomain);
        when(usuarioJpaRepository.findByEmail(email)).thenReturn(Optional.of(entityMock));
        when(jwtService.generateToken(email, nombre)).thenReturn(fakeToken);

        when(refreshTokenRepository.save(any(RefreshTokenEntity.class))).thenReturn(mock(RefreshTokenEntity.class));

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(fakeToken))
                .andExpect(jsonPath("$.nombre").value(nombre))
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void refresh_ShouldReturn200_AndNewToken_WhenRefreshTokenIsValid() throws Exception {
        // Arrange
        String refreshToken = "uuid-valido-123";
        String newAccessToken = "nuevo-token-generado";

        RefreshRequestDTO request = new RefreshRequestDTO(refreshToken);

        when(refreshTokenService.refresh(refreshToken)).thenReturn(newAccessToken);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(newAccessToken));
    }
}