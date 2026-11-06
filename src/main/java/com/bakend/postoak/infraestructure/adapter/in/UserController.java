package com.bakend.postoak.infraestructure.adapter.in;

import com.bakend.postoak.domain.model.User;
import com.bakend.postoak.domain.port.in.GetUserAuthenticatedUseCase;
import com.bakend.postoak.domain.port.in.AddUserUseCase;
import com.bakend.postoak.domain.dto.AddUserRequestDTO;
import com.bakend.postoak.domain.dto.UserResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UserController {

    private final AddUserUseCase addUserUseCase;
    private final GetUserAuthenticatedUseCase getUserAuthenticatedUseCase;

    public UserController(AddUserUseCase addUserUseCase, GetUserAuthenticatedUseCase getUserAuthenticatedUseCase) {
        this.addUserUseCase = addUserUseCase;
        this.getUserAuthenticatedUseCase = getUserAuthenticatedUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registrar(@RequestBody AddUserRequestDTO request) {

        // Creamos el objeto User con los nuevos atributos del ERD
        User user = new User(
                UUID.randomUUID(),               // id generado
                request.email(),
                request.password(),              // (Nota: asegúrate de encriptarla en tu caso de uso con BCrypt)
                request.fullName(),              // actualizado a fullName
                request.companyName(),           // añadido companyName
                LocalDateTime.now()
        );

        addUserUseCase.register(user);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(Authentication authentication) {

        String email = authentication.getName(); // ← sub del JWT

        User user = getUserAuthenticatedUseCase.getCurrentUser(email);

        return ResponseEntity.ok(
                new UserResponseDTO(
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getCompanyName()
                )
        );
    }
}