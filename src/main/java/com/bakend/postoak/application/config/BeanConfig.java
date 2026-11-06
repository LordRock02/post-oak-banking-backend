package com.bakend.postoak.application.config;

import com.bakend.postoak.application.service.LoginUserService;
import com.bakend.postoak.application.service.AddUserService;
import com.bakend.postoak.domain.port.in.LoginUserUseCase;
import com.bakend.postoak.domain.port.in.AddUserUseCase;
import com.bakend.postoak.domain.port.out.UserRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfig {

    @Bean
    public AddUserUseCase registrarUsuarioUseCase(
            UserRepositoryPort userRepositoryPort,
            PasswordEncoder passwordEncoder
    ) {
        return new AddUserService(userRepositoryPort, passwordEncoder);
    }

    @Bean
    public LoginUserUseCase loginUsuarioUseCase(
            UserRepositoryPort userRepositoryPort,
            PasswordEncoder passwordEncoder
    ) {
        return new LoginUserService(userRepositoryPort, passwordEncoder);
    }
}
