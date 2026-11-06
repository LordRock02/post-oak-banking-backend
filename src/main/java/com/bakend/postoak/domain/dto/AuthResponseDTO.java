package com.bakend.postoak.domain.dto;

public record AuthResponseDTO(String accessToken,
                              String refreshToken,
                              String nombre) {}

