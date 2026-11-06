package com.bakend.postoak.infraestructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RefreshTokenEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String token;

    @ManyToOne
    private UserEntity usuario;

    private Instant expiryDate;
}
