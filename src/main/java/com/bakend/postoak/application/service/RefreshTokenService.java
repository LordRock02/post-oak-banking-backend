package com.bakend.postoak.application.service;

import com.bakend.postoak.domain.port.in.RefreshTokenUseCase;
import com.bakend.postoak.infraestructure.persistence.RefreshTokenEntity;
import com.bakend.postoak.infraestructure.persistence.RefreshTokenJpaRepository;
import com.bakend.postoak.infraestructure.persistence.UserEntity;
import com.bakend.postoak.infraestructure.security.JwtService;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RefreshTokenService implements RefreshTokenUseCase {

    private final RefreshTokenJpaRepository refreshTokenRepository;
    private final JwtService jwtService;

    public RefreshTokenService(
            RefreshTokenJpaRepository refreshTokenRepository,
            JwtService jwtService
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    @Override
    public String refresh(String refreshToken) {
        RefreshTokenEntity tokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token inválido"));

        if (tokenEntity.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expirado");
        }

        UserEntity usuario = tokenEntity.getUsuario();

        return jwtService.generateToken(usuario.getEmail(), usuario.getFullName());
    }

}
