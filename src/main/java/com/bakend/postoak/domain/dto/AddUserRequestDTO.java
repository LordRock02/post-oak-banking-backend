package com.bakend.postoak.domain.dto;

public record AddUserRequestDTO(
        String fullName,
        String email,
        String password,
        String companyName
) {}

