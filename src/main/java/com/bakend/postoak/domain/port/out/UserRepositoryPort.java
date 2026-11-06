package com.bakend.postoak.domain.port.out;

import com.bakend.postoak.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort {
    void save(User user);
    Optional<User> findByEmail(String email);

}