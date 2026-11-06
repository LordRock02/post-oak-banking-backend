package com.bakend.postoak.infraestructure.adapter.out;

import com.bakend.postoak.domain.model.User;
import com.bakend.postoak.domain.port.out.UserRepositoryPort;
import com.bakend.postoak.infraestructure.persistence.SpringDataUserRepository;
import com.bakend.postoak.infraestructure.persistence.UserEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class UserRepositoryJpaAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository repository;

    public UserRepositoryJpaAdapter(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(User user) {
        // Si user.getId() viene inicializado pero es un registro nuevo,
        // pasamos null para que la base de datos o @GeneratedValue cree el UUID.
        UserEntity entity = new UserEntity(
                null, // Cambiado aquí para forzar el alta como nuevo registro
                user.getEmail(),
                user.getPasswordHash(),
                user.getFullName(),
                user.getCompanyName(),
                user.getCreatedAt() != null ? user.getCreatedAt() : LocalDateTime.now()
        );
        repository.save(entity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        // Mapeamos de la entidad JPA UsuarioEntity de vuelta al modelo de dominio User
        return repository.findByEmail(email)
                .map(entity -> new User(
                        entity.getId(),
                        entity.getEmail(),
                        entity.getPasswordHash(),
                        entity.getFullName(),
                        entity.getCompanyName(), // o getCompanyName() según tus getters de lombok
                        entity.getCreatedAt()
                ));
    }
}