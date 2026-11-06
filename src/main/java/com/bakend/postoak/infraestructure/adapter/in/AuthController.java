package com.bakend.postoak.infraestructure.adapter.in;

import com.bakend.postoak.application.service.RefreshTokenService;
import com.bakend.postoak.domain.model.User;
import com.bakend.postoak.domain.port.in.LoginUserUseCase;
import com.bakend.postoak.domain.dto.AuthResponseDTO;
import com.bakend.postoak.domain.dto.LoginRequestDTO;
import com.bakend.postoak.domain.dto.RefreshRequestDTO;
import com.bakend.postoak.domain.dto.RefreshResponseDTO;
import com.bakend.postoak.infraestructure.persistence.RefreshTokenEntity;
import com.bakend.postoak.infraestructure.persistence.RefreshTokenJpaRepository;
import com.bakend.postoak.infraestructure.persistence.SpringDataUserRepository;
import com.bakend.postoak.infraestructure.persistence.UserEntity;
import com.bakend.postoak.infraestructure.security.JwtService;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final LoginUserUseCase loginUserUseCase;
    private final JwtService jwtService;
    private final RefreshTokenJpaRepository refreshTokenRepository;
    private final SpringDataUserRepository usuarioJpaRepository;
    private final RefreshTokenService refreshTokenService;

    public AuthController(
            LoginUserUseCase loginUserUseCase,
            JwtService jwtService,
            RefreshTokenJpaRepository refreshTokenRepository,
            SpringDataUserRepository usuarioJpaRepository,
            RefreshTokenService refreshTokenService
    ) {
        this.loginUserUseCase = loginUserUseCase;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO request) {

        User user = loginUserUseCase.login(
                request.email(),
                request.password()
        );

        UserEntity userEntity = usuarioJpaRepository
                .findByEmail(user.getEmail())
                .orElseThrow();

        // Corregido: se usa getFullName() en lugar de getNombre()
        String accessToken = jwtService.generateToken(userEntity.getEmail(), userEntity.getFullName());

        String refreshToken = UUID.randomUUID().toString();

        refreshTokenRepository.save(
                new RefreshTokenEntity(
                        null,
                        refreshToken,
                        userEntity,
                        Instant.now().plus(7, ChronoUnit.DAYS)
                )
        );

        return new AuthResponseDTO(
                accessToken,
                refreshToken,
                user.getFullName() // Corregido: se usa getFullName()
        );
    }

    @PostMapping("/refresh")
    public RefreshResponseDTO refresh(@RequestBody RefreshRequestDTO request) {
        String newAccessToken = refreshTokenService.refresh(request.refreshToken());
        return new RefreshResponseDTO(newAccessToken);
    }
}