package com.bakend.postoak.aplication;

import com.bakend.postoak.domain.dto.AddUserRequestDTO;
import com.bakend.postoak.domain.model.User;
import com.bakend.postoak.domain.port.in.GetUserAuthenticatedUseCase;
import com.bakend.postoak.domain.port.in.AddUserUseCase;
import com.bakend.postoak.infraestructure.adapter.in.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private AddUserUseCase addUserUseCase;

    @Mock
    private GetUserAuthenticatedUseCase getUserAuthenticatedUseCase;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    void registrarUsuario_debeRetornar201() throws Exception {
        AddUserRequestDTO request =
                new AddUserRequestDTO("Pablo Perez", "pablo@mail.com", "1234", "Post Oak");

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Actualizado a registrar() que es el método del puerto de entrada
        verify(addUserUseCase).register(any(User.class));
    }

    @Test
    void obtenerUsuarioAutenticado_debeRetornar200() throws Exception {
        // Inicializado con los campos completos del ERD (id, email, passwordHash, fullName, companyName, createdAt)
        User user = new User(
                UUID.randomUUID(),
                "pablo@mail.com",
                "1234",
                "Pablo Perez",
                "Post Oak",
                LocalDateTime.now()
        );

        when(getUserAuthenticatedUseCase.getCurrentUser("pablo@mail.com"))
                .thenReturn(user);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("pablo@mail.com", null);

        mockMvc.perform(get("/api/v1/usuarios/me")
                        .principal(authentication))
                .andExpect(status().isOk());
    }
}