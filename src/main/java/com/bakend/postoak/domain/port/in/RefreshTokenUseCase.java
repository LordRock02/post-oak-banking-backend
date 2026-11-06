package com.bakend.postoak.domain.port.in;

public interface RefreshTokenUseCase {
    String refresh(String refreshToken);
}
